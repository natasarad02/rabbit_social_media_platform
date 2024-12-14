package rs.ac.uns.ftn.informatika.jpa.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.jpa.dto.ProfileDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.ProfileViewDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.primer.StudentDTO;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;
import rs.ac.uns.ftn.informatika.jpa.model.Role;
import rs.ac.uns.ftn.informatika.jpa.model.primer.Student;
import rs.ac.uns.ftn.informatika.jpa.service.PostService;
import rs.ac.uns.ftn.informatika.jpa.service.ProfileService;
import rs.ac.uns.ftn.informatika.jpa.utils.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;


@RestController
@RequestMapping(value = "api/profiles")
public class ProfileController {

    private ProfileService profileService;
    private PostService postService;
    private final TokenUtils tokenUtils;
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    public ProfileController(@Autowired ProfileService profileService, @Autowired PostService postService) {
        this.profileService = profileService;
        this.postService = postService;
        this.tokenUtils = new TokenUtils();
    }

    @GetMapping(value = "/all")
    @PreAuthorize("hasAuthority('Administrator')")
    public ResponseEntity<List<ProfileViewDTO>> getAllProfiles() {

        List<Profile> profiles = profileService.getAllProfilesWithFollowersAndPosts();


        List<ProfileViewDTO> profileViewDTOs = new ArrayList<>();
        for (Profile profile : profiles) {
            if (profile.getRole() == Role.User) {
                ProfileViewDTO profileViewDTO = new ProfileViewDTO();
                profileViewDTO.setId(profile.getId());
                profileViewDTO.setName(profile.getName());
                profileViewDTO.setEmail(profile.getEmail());
                profileViewDTO.setSurname(profile.getSurname());
                profileViewDTO.setFollowingCount(profile.getFollowers().size());
                profileViewDTO.setPostCount(postService.countPostsForProfile(profile.getId()));
                profileViewDTOs.add(profileViewDTO);
            }
        }


        return new ResponseEntity<>(profileViewDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/allPaged")
    @PreAuthorize("hasAuthority('Administrator')")
    public ResponseEntity<Page<ProfileViewDTO>> getAllProfiles(Pageable pageable, @RequestParam("profileIds") List<Integer> profileIds) {
        Page<Profile> profiles = profileService.getAllProfilesWithFollowersAndPosts(pageable, profileIds);

        List<ProfileViewDTO> profileViewDTOs = new ArrayList<>();
        for (Profile profile : profiles.getContent()) {

            ProfileViewDTO profileViewDTO = new ProfileViewDTO();
            profileViewDTO.setId(profile.getId());
            profileViewDTO.setName(profile.getName());
            profileViewDTO.setEmail(profile.getEmail());
            profileViewDTO.setSurname(profile.getSurname());
            profileViewDTO.setFollowingCount(profile.getFollowers().size());
            profileViewDTO.setPostCount(postService.countPostsForProfile(profile.getId()));
            profileViewDTOs.add(profileViewDTO);

        }



        return new ResponseEntity<>(new PageImpl<>(profileViewDTOs, pageable, profiles.getTotalElements()), HttpStatus.OK);
    }

    //new
    @GetMapping("/allNew")
    @PreAuthorize("hasAuthority('Administrator')")
    public ResponseEntity<Page<ProfileViewDTO>> getProfiles(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer minPosts,
            @RequestParam(required = false) Integer maxPosts,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<Profile> profiles = profileService.getProfiles(name, surname, email, minPosts, maxPosts, sortBy, sortDirection, page, size);
        List<ProfileViewDTO> profileViewDTOs = new ArrayList<>();
        for (Profile profile : profiles.getContent()) {
            ProfileViewDTO profileViewDTO = new ProfileViewDTO();
            profileViewDTO.setId(profile.getId());
            profileViewDTO.setName(profile.getName());
            profileViewDTO.setEmail(profile.getEmail());
            profileViewDTO.setSurname(profile.getSurname());
            profileViewDTO.setFollowingCount(profile.getFollowers().size());
            profileViewDTO.setPostCount(postService.countPostsForProfile(profile.getId()));
            profileViewDTOs.add(profileViewDTO);

        }


        return new ResponseEntity<>(new PageImpl<>(profileViewDTOs), HttpStatus.OK);
    }


    @GetMapping(value = "/followers/{id}")
    @PreAuthorize("hasAuthority('User')")
    public ResponseEntity<List<ProfileDTO>> getFollowersForProfile(@PathVariable Integer id) {
        logger.info("Fetching followers for profile ID: {}", id);

        Profile profile = profileService.findOne(id);
        if (profile == null) {
            logger.warn("Profile not found for ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.info("Profile found: {}", profile.getName()); // Example if Profile has a `getName` method
        List<ProfileDTO> followerDTOs = new ArrayList<>();
        Set<Profile> followers = profile.getFollowers();

        if (followers == null || followers.isEmpty()) {
            logger.info("No followers found for profile ID: {}", id);
        } else {
            logger.info("Found {} followers for profile ID: {}", followers.size(), id);
        }

        for (Profile p : followers) {
            logger.debug("Adding follower to list: {}", p.getName()); // Example if Profile has a `getName` method
            followerDTOs.add(new ProfileDTO(p));
        }

        return new ResponseEntity<>(followerDTOs, HttpStatus.OK);
    }



    @GetMapping(value = "/following/{id}")
    @PreAuthorize("hasAuthority('User')")
    public ResponseEntity<List<ProfileDTO>> getFollowingsForProfile(@PathVariable Integer id) {
        logger.info("Fetching followings for profile ID: {}", id);

        Profile profile = profileService.findOne(id);
        if (profile == null) {
            logger.warn("Profile not found for ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.info("Profile found: {}", profile.getName()); // Example if Profile has a `getName` method
        List<ProfileDTO> followingDTOs = new ArrayList<>();
        Set<Profile> followings = profile.getFollowing(); // Note: Fixed the incorrect `getFollowers()` call

        if (followings == null || followings.isEmpty()) {
            logger.info("No followings found for profile ID: {}", id);
        } else {
            logger.info("Found {} followings for profile ID: {}", followings.size(), id);
        }

        for (Profile p : followings) {
            logger.debug("Adding following to list: {}", p.getName()); // Example if Profile has a `getName` method
            followingDTOs.add(new ProfileDTO(p));
        }

        return new ResponseEntity<>(followingDTOs, HttpStatus.OK);
    }




    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('User', 'Administrator')")
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable Integer id) {
        Profile profile = profileService.findOne(id);
        ProfileDTO profileDTO = new ProfileDTO(profile);
        if (profile == null) {

            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(profileDTO);
    }



    @GetMapping("/activate")
    public ResponseEntity<String> activateUser(@RequestParam("token") String token) {
        try {
            System.out.println("Received token: " + token);

            if (profileService.activateUser(token)) {
                return ResponseEntity.ok("Account activated successfully.");
            } else {
                return ResponseEntity.badRequest().body("Activation failed. User not found or already activated.");
            }
        } catch (Exception e) {
            // Handle invalid token case
            return ResponseEntity.badRequest().body("Invalid activation token."+ e.getMessage());
        }
    }

    @PostMapping("/follow")
    @PreAuthorize("hasAnyAuthority('User', 'Administrator')")
    public ResponseEntity<Void> followProfile(@RequestParam Integer profileId, @RequestParam Integer followedProfileId) {
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);
        profileService.followProfile(profileId, followedProfileId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unfollow")
    @PreAuthorize("hasAnyAuthority('User', 'Administrator')")
    public ResponseEntity<Void> unfollowProfile(@RequestParam Integer profileId, @RequestParam Integer followedProfileId) {
        profileService.unfollowProfile(profileId, followedProfileId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/followers/{id}")
    @PreAuthorize("hasAnyAuthority('User', 'Administrator')")
    public ResponseEntity<List<ProfileDTO>> getFollowers(@PathVariable Integer id) {
        List<Profile> profiles = profileService.getFollowers(id);

        if (profiles == null || profiles.isEmpty()) {
            return null;
        }

        List<ProfileDTO> profileDTOs = profiles.stream()
                .map(ProfileDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(profileDTOs);
    }


}
