package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.dto.util.UserRequest;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;
import rs.ac.uns.ftn.informatika.jpa.model.Role;
import rs.ac.uns.ftn.informatika.jpa.model.primer.Student;
import rs.ac.uns.ftn.informatika.jpa.repository.ProfileRepository;

import java.util.HashSet;
import java.util.List;
@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public Page<Profile> getAllProfilesWithFollowersAndPosts(Pageable pageable) {

        Page<Profile> profiles = profileRepository.findAllActiveProfiles(Role.User ,pageable);

        for (Profile profile : profiles) {

            List<Profile> followingProfiles = profileRepository.findFollowingProfiles(profile.getId());
            profile.setFollowers(new HashSet<>(followingProfiles));

        }

        return profiles;
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
        profileToSave.setId(profileRepository.findMaxId()+1);
        return profileRepository.save(profileToSave);
    }
}
