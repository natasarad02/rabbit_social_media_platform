package rs.ac.uns.ftn.informatika.jpa.service;

import com.google.common.hash.BloomFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.informatika.jpa.dto.ProfileDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.ProfileViewDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.util.UserRequest;
import rs.ac.uns.ftn.informatika.jpa.exception.ResourceConflictException;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;
import rs.ac.uns.ftn.informatika.jpa.model.Role;
import rs.ac.uns.ftn.informatika.jpa.repository.ProfileRepository;
import rs.ac.uns.ftn.informatika.jpa.utils.TokenUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProfileService {
    private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);

    private final ProfileRepository profileRepository;
    private final ActiveUsersMetricService activeUsersMetricService;
    private final BloomFilter<String> bloomFilter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TokenUtils tokenUtils;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, ActiveUsersMetricService activeUsersMetricService, BloomFilter<String> bloomFilter) {
        this.profileRepository = profileRepository;
        this.activeUsersMetricService = activeUsersMetricService;
        this.bloomFilter = bloomFilter;
    }

    @Scheduled(fixedRate = 5000)
    public void updateActiveUserCount() {
        int activeUsers = profileRepository.countByIsActive(true);
        activeUsersMetricService.setActiveUserCount(activeUsers);
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
        } else {
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

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allProfiles.size());
        List<Profile> pagedProfiles = allProfiles.subList(start, end);

        for (Profile profile : pagedProfiles) {
            List<Profile> followingProfiles = profileRepository.findFollowingProfiles(profile.getId());
            profile.setFollowers(new HashSet<>(followingProfiles));
        }

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

    // za proveru svaki minut @Scheduled(cron = "0 * * * * ?")
    @Scheduled(cron = "0 0 0 L * ?") // Runs at midnight on the last day of every month
    public void cleanupUnactivatedProfiles() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusMonths(1);
        List<Profile> unactivatedProfiles = profileRepository.findUnactivatedProfilesBefore(cutoffDate);

        if (!unactivatedProfiles.isEmpty()) {
            profileRepository.deleteAll(unactivatedProfiles);
            System.out.println("Deleted " + unactivatedProfiles.size() + " unactivated profiles.");
        }
    }

    //new
    public Page<Profile> getProfiles(String name, String surname, String email, Integer minPosts, Integer maxPosts,
                                            String sortBy, String sortDirection, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), sortBy));

        Specification<Profile> specification = Specification.where(null);

        if (name != null && !name.isEmpty()) {
            specification = specification.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if (surname != null && !surname.isEmpty()) {
            specification = specification.and((root, query, cb) -> cb.like(cb.lower(root.get("surname")), "%" + surname.toLowerCase() + "%"));
        }
        if (email != null && !email.isEmpty()) {
            specification = specification.and((root, query, cb) -> cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
        }
        if (minPosts != null) {
            specification = specification.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("postCount"), minPosts));
        }
        if (maxPosts != null) {
            specification = specification.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("postCount"), maxPosts));
        }

        return profileRepository.findAll(specification, pageable);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void followProfile(Integer profileId, Integer followedProfileId)
    {
        Profile profile = profileRepository.findById(profileId).orElse(null);
        if(profile == null)
        {
            return;
        }


        LocalDateTime now = LocalDateTime.now();

        // Proverava da li je poslednji bio pre minut
        if (profile.getLastFollowTime() == null || profile.getLastFollowTime().isBefore(now.minusMinutes(1))) {
            profile.setMinute_following(1); // Reset counter
            profile.setLastFollowTime(now);
        } else {
            // Inkeremntira koliko je u minuti
            if (profile.getMinute_following() >= 50) {
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "You can follow a maximum of 50 profiles per minute.");
            }
            profile.setMinute_following(profile.getMinute_following() + 1);
        }

        profileRepository.save(profile);

        // Proceed with the follow action
        logger.info("> follow");
        profileRepository.followProfile(profileId, followedProfileId);
        logger.info("< follow");

    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void unfollowProfile(Integer profileId, Integer followedProfileId)
    {
        logger.info("> unfollow");
        profileRepository.unfollowProfile(profileId, followedProfileId);
        logger.info("< unfollow");
    }

    public List<Profile> getFollowers(@Param("profileId") Integer profileId)
    {
        return profileRepository.getFollowers(profileId);
    }

    public void save(Profile profile)
    {
        profileRepository.save(profile);
    }

    public Profile updateProfileCurrentlyActiveStatus(Integer id) {
        Profile profile = findOne(id);

        if (profile == null) {
            return null;
        }

        profile.setActive(false);
        return profileRepository.save(profile);
    }

    public String getUsernameById(int id)
    {
        Profile profile = profileRepository.findById(id).orElse(null);
        if(profile == null)
        {
            return "";
        }
        else return profile.getUsername();
    }

    public Profile findById(int id)
    {
        return profileRepository.findById(id).orElse(null);
    }
}