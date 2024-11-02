package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;
import rs.ac.uns.ftn.informatika.jpa.model.primer.Student;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Student, Integer> {
    @Query("SELECT p FROM Profile p WHERE p.deleted = false")
    List<Profile> findAllActiveProfiles();

    @Query("SELECT f FROM Profile p JOIN p.following f WHERE p.id = :profileId AND p.deleted = false")
    List<Profile> findFollowingProfiles(@Param("profileId") Integer profileId);

}
