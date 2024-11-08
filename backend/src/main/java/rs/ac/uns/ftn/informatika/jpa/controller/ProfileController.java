package rs.ac.uns.ftn.informatika.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<Page<ProfileViewDTO>> getAllProfiles(Pageable pageable) {
        Page<Profile> profiles = profileService.getAllProfilesWithFollowersAndPosts(pageable);

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

}
