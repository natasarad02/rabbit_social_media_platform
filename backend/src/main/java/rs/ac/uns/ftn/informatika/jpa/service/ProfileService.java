package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.dto.ProfileDTO;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;
import rs.ac.uns.ftn.informatika.jpa.model.Role;
import rs.ac.uns.ftn.informatika.jpa.model.primer.Student;
import rs.ac.uns.ftn.informatika.jpa.repository.ProfileRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
@Service
public class ProfileService {
    private final ProfileRepository profileRepository;

    public ProfileService(@Autowired ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Profile findOne(Integer id) {
        return profileRepository.findById(id).orElseGet(null);
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





    public Profile saveProfile(Profile profile) {
        return profileRepository.save(profile);
    }
}
