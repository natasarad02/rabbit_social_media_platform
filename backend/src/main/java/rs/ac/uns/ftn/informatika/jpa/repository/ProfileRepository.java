package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
=======
>>>>>>> parent of 503dbad (Merge branch 'student2' into merging)
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional; // Use Spring's Transactional
import rs.ac.uns.ftn.informatika.jpa.model.Profile;
import rs.ac.uns.ftn.informatika.jpa.model.Role;
<<<<<<< HEAD
=======
import rs.ac.uns.ftn.informatika.jpa.model.primer.Student;
>>>>>>> parent of 503dbad (Merge branch 'student2' into merging)

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

<<<<<<< HEAD
public interface ProfileRepository extends JpaRepository<Profile, Integer>, JpaSpecificationExecutor<Profile> {

    Integer countAllByDeleted(boolean deleted);

    Integer countByIsActive(boolean isActive);

    @Query("SELECT COUNT(p) FROM Profile p WHERE EXISTS (SELECT 1 FROM Post post WHERE post.profile = p)")
    Integer countProfilesWithPosts();

    @Query("SELECT COUNT(p) FROM Profile p " +
            "WHERE EXISTS (SELECT 1 FROM Comment c WHERE c.profile = p) " +
            "AND NOT EXISTS (SELECT 1 FROM Post post WHERE post.profile = p)")
    long countProfilesWithCommentWithoutPosts();

=======
public interface ProfileRepository extends JpaRepository<Profile, Integer> {
>>>>>>> parent of 503dbad (Merge branch 'student2' into merging)
    @Query("SELECT p FROM Profile p WHERE p.deleted = false")
    List<Profile> findAllActiveProfiles();

    @Query("SELECT p.username FROM Profile p WHERE p.deleted = false")
    Stream<String> findAllActiveProfilesUsernames();

    @Query("SELECT f FROM Profile p JOIN p.following f WHERE p.id = :profileId AND p.deleted = false")
    List<Profile> findFollowingProfiles(@Param("profileId") Integer profileId);

    @Query("SELECT p FROM Profile p WHERE p.deleted = false AND p.role = :role")
    Page<Profile> findAllActiveProfiles(@Param("role") Role role, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Profile p SET p.deleted = true WHERE p.username = :username AND p.deleted = false")
    void deleteByUsername(@Param("username") String username);

    @Query("SELECT COUNT(p) FROM Profile p WHERE p.username = :username AND p.deleted = false")
    long countActiveByUsername(@Param("username") String username);

    @Query("SELECT p FROM Profile p WHERE p.deleted = false AND p.role = :role AND p.id IN :profileIds")
    List<Profile> findAllActiveProfilesSorted(@Param("role") Role role, @Param("profileIds") List<Integer> profileIds);

    @Query("SELECT p FROM Profile p WHERE p.email = :email AND p.deleted = false")
    Profile findActiveProfileByEmail(@Param("email") String email);

    // Fetch following profiles for a specific profile with pagination
    @Query("SELECT f FROM Profile p JOIN p.following f WHERE p.id = :profileId AND p.deleted = false")
    Page<Profile> findFollowingProfiles(@Param("profileId") Integer profileId, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Profile p WHERE p.username = :username AND p.deleted = false")
    Optional<Profile> findActiveProfileByUsername2(@Param("username") String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p.id FROM Profile p WHERE p.username = :username AND p.deleted = false")
    Optional<Long> findIdByUsername(@Param("username") String username);



    @Query("SELECT p FROM Profile p WHERE p.username = :username AND p.deleted = false")
    Profile findActiveProfileByUsername(@Param("username") String username);

    @Query("SELECT MAX(p.id) FROM Profile p")
    Integer findMaxId();

<<<<<<< HEAD
    @Query("SELECT p FROM Profile p WHERE p.activated = false AND p.registrationTime < :cutoffDate")
    List<Profile> findUnactivatedProfilesBefore(@Param("cutoffDate") LocalDateTime cutoffDate);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO profile_following (profile_id, followed_profile_id) VALUES (:profileId, :followedProfileId)", nativeQuery = true)
    void followProfile(@Param("profileId") Integer profileId, @Param("followedProfileId") Integer followedProfileId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM profile_following WHERE profile_id = :profileId AND followed_profile_id = :followedProfileId", nativeQuery = true)
    void unfollowProfile(@Param("profileId") Integer profileId, @Param("followedProfileId") Integer followedProfileId);

    @Query(value = "SELECT p.* FROM profile p " +
            "JOIN profile_following pf ON p.id = pf.followed_profile_id " +
            "WHERE pf.profile_id = :profileId", nativeQuery = true)
    List<Profile> getFollowers(@Param("profileId") Integer profileId);
}
=======
}
>>>>>>> parent of 503dbad (Merge branch 'student2' into merging)
