package rs.ac.uns.ftn.informatika.jpa.service;

import com.google.common.hash.BloomFilter;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.informatika.jpa.dto.util.UserRequest;
import rs.ac.uns.ftn.informatika.jpa.dto.ProfileDTO;
import rs.ac.uns.ftn.informatika.jpa.exception.ResourceConflictException;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;
import rs.ac.uns.ftn.informatika.jpa.model.Role;
import rs.ac.uns.ftn.informatika.jpa.model.primer.Student;
import rs.ac.uns.ftn.informatika.jpa.repository.ProfileRepository;
import rs.ac.uns.ftn.informatika.jpa.utils.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Service
public class ProfileService {
    private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);
    private final ProfileRepository profileRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TokenUtils tokenUtils;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final BloomFilter<String> bloomFilter;

    public ProfileService(@Autowired ProfileRepository profileRepository, BloomFilter<String> bloomFilter) {
        this.profileRepository = profileRepository;
        this.bloomFilter = bloomFilter;
    }

    @Transactional
    public Profile createProfile(UserRequest userRequest) {
        String username = userRequest.getUsername();
        if(bloomFilter.mightContain(userRequest.getUsername())) {
            logger.warn("Bloom filter suspects username '{}' might exist. Falling back to DB check with pessimistic lock.", username);
            profileRepository.findIdByUsername(username)
                    .ifPresent(existingId -> {
                        logger.error("Pessimistic lock confirmed that username '{}' with ID {} already exists.", username, existingId);
                        throw new ResourceConflictException(userRequest.getId(), "Username already exists");
                    });
            logger.info("False positive in Bloom filter for username '{}', proceeding with registration.", username);
        }
        else{
            logger.info("Bloom filter confirms username '{}' is new. Skipping DB check.", username);
        }
        try {

            Profile profileToSave = new Profile();
            profileToSave.setUsername(userRequest.getUsername());
            profileToSave.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            profileToSave.setRole(Role.User);
            profileToSave.setFollowers(new HashSet<>());
            profileToSave.setPosts(new HashSet<>());
            profileToSave.setEmail(userRequest.getEmail());
            profileToSave.setName(userRequest.getFirstname());
            profileToSave.setSurname(userRequest.getLastname());
            profileToSave.setAddress(userRequest.getAddress());

            Profile savedProfile = profileRepository.save(profileToSave);

            bloomFilter.put(savedProfile.getUsername());
            logger.info("Successfully registered '{}' and added to Bloom filter.", savedProfile.getUsername());

            return savedProfile;
        } catch (DataIntegrityViolationException e) {
            throw new ResourceConflictException(userRequest.getId(), "Username already exists (via constraint)");
        }
    }

    @Transactional(readOnly = true)
    public Profile findOne(Integer id) {
        return profileRepository.findById(id).orElseGet(null);
    }

    public Profile findByUsername(String username) {
        return profileRepository.findActiveProfileByUsername(username);
    }

    public List<Profile> getAllProfilesWithFollowersAndPosts() {
        List<Profile> profiles = profileRepository.findAllActiveProfiles();
        for (Profile profile : profiles) {
            List<Profile> followingProfiles = profileRepository.findFollowingProfiles(profile.getId());
            profile.setFollowers(new HashSet<>(followingProfiles));
        }
        return profiles;
    }

    public Page<Profile> getAllProfilesWithFollowersAndPosts(Pageable pageable, List<Integer> orderedProfileIds) {


        List<Profile> allProfiles = profileRepository.findAllActiveProfilesSorted(Role.User, orderedProfileIds);


        allProfiles.sort(Comparator.comparingInt(profile -> orderedProfileIds.indexOf(profile.getId())));

        // Apply pagination manually
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allProfiles.size());
        List<Profile> pagedProfiles = allProfiles.subList(start, end);

        // Set followers for each profile in the paged list
        for (Profile profile : pagedProfiles) {
            List<Profile> followingProfiles = profileRepository.findFollowingProfiles(profile.getId());
            profile.setFollowers(new HashSet<>(followingProfiles));
        }

        // Return the paged result wrapped in a Page object
        return new PageImpl<>(pagedProfiles, pageable, allProfiles.size());
    }

    public boolean updateProfile(ProfileDTO profileDTO) {
        Optional<Profile> optionalProfile = profileRepository.findById(profileDTO.getId());

        if (optionalProfile.isPresent()) {
            Profile profile = optionalProfile.get();

            // Update only allowed fields
            profile.setName(profileDTO.getName());
            profile.setSurname(profileDTO.getSurname());
            profile.setAddress(profileDTO.getAddress());

            // Save the updated profile
            profileRepository.save(profile);

            return true; // Indicates success
        }

        return false; // Indicates profile not found
    }

    public boolean updatePassword(int profileId, String newPassword) {
        Optional<Profile> optionalProfile = profileRepository.findById(profileId);

        if (optionalProfile.isPresent()) {
            Profile profile = optionalProfile.get();

            // Validate the new password (e.g., length, strength)
            if (newPassword.length() < 8) {
                return false; // Password must be at least 8 characters (for example)
            }

            // Encrypt the new password
            String encodedPassword = passwordEncoder.encode(newPassword);

            // Update the profile with the new password
            profile.setPassword(encodedPassword);
            profileRepository.save(profile);

            return true;
        }

        return false; // Profile not found
    }

    public void sendActivationEmail(Profile user) {
        String token = tokenUtils.generateActivationToken(user.getEmail());
        String activationLink = "http://localhost:8080/api/profiles/activate?token=" + token;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom(fromEmail);
        mailMessage.setText("To activate your account, click here: " + activationLink);

        mailSender.send(mailMessage);
    }

    public boolean activateUser(String token) {
        if (tokenUtils.isTokenExpired(token)) {
            return false;
        }
        String email = tokenUtils.extractEmail(token);
        Profile user = profileRepository.findActiveProfileByEmail(email);
        if (user != null && !user.isActivated()) {
            user.setActivated(true);
            profileRepository.save(user);
            return true;
        }
        return false;
    }
}
