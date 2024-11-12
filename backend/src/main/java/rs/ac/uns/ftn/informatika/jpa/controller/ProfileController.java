package rs.ac.uns.ftn.informatika.jpa.controller;

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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "api/profiles")
public class ProfileController {

    private ProfileService profileService;
    private PostService postService;

    public ProfileController(@Autowired ProfileService profileService, @Autowired PostService postService) {
        this.profileService = profileService;
        this.postService = postService;
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
    public ResponseEntity<String> activateUser(@RequestParam("email") String email) {
        if (profileService.activateUser(email)) {
            return ResponseEntity.ok("Account activated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Activation failed. User not found or already activated.");
        }
    }
}
