package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;
import rs.ac.uns.ftn.informatika.jpa.model.Role;
import rs.ac.uns.ftn.informatika.jpa.model.primer.Student;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    @Query("SELECT p FROM Profile p WHERE p.deleted = false")
    List<Profile> findAllActiveProfiles();

    @Query("SELECT f FROM Profile p JOIN p.following f WHERE p.id = :profileId AND p.deleted = false")
    List<Profile> findFollowingProfiles(@Param("profileId") Integer profileId);

    @Query("SELECT p FROM Profile p WHERE p.deleted = false AND p.role = :role")
    Page<Profile> findAllActiveProfiles(@Param("role") Role role, Pageable pageable);

    // Fetch following profiles for a specific profile with pagination
    @Query("SELECT f FROM Profile p JOIN p.following f WHERE p.id = :profileId AND p.deleted = false")
    Page<Profile> findFollowingProfiles(@Param("profileId") Integer profileId, Pageable pageable);

    @Query("SELECT p FROM Profile p WHERE p.username = :username AND p.deleted = false")
    Profile findActiveProfileByUsername(@Param("username") String username);

    @Query("SELECT MAX(p.id) FROM Profile p")
    Integer findMaxId();

}
