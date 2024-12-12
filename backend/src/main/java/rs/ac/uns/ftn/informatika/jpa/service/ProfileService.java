package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.informatika.jpa.dto.ProfileViewDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.util.UserRequest;
import rs.ac.uns.ftn.informatika.jpa.dto.ProfileDTO;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;
import rs.ac.uns.ftn.informatika.jpa.model.Role;
import rs.ac.uns.ftn.informatika.jpa.model.primer.Student;
import rs.ac.uns.ftn.informatika.jpa.repository.ProfileRepository;
import rs.ac.uns.ftn.informatika.jpa.utils.TokenUtils;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TokenUtils tokenUtils;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public ProfileService(@Autowired ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
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


    public Profile saveProfile(UserRequest profile)
    {
        // proveriti da li su sva polja namestena
        Profile profileToSave = new Profile();
        profileToSave.setUsername(profile.getUsername());
        profileToSave.setPassword(passwordEncoder.encode(profile.getPassword()));
        profileToSave.setRole(Role.User);
        profileToSave.setFollowers(new HashSet<>());
        profileToSave.setPosts(new HashSet<>());
        profileToSave.setEmail(profile.getEmail());
        profileToSave.setName(profile.getFirstname());
        profileToSave.setSurname(profile.getLastname());
        profileToSave.setAddress(profile.getAddress());
        return profileRepository.save(profileToSave);
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
        profileRepository.followProfile(profileId, followedProfileId);

    }

    public void unfollowProfile(Integer profileId, Integer followedProfileId)
    {
        profileRepository.unfollowProfile(profileId, followedProfileId);
    }

}
