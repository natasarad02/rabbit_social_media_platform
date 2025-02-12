package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;
import rs.ac.uns.ftn.informatika.jpa.model.Role;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Integer>, JpaSpecificationExecutor<Profile> {

    Integer countAllByDeleted(boolean deleted);

    Integer countByIsActive(boolean isActive);
    @Query("SELECT COUNT(p) FROM Profile p WHERE EXISTS (SELECT 1 FROM Post post WHERE post.profile = p)")
    Integer countProfilesWithPosts();

    @Query("SELECT COUNT(p) FROM Profile p " +
            "WHERE EXISTS (SELECT 1 FROM Comment c WHERE c.profile = p) " +  // Profile has a comment
            "AND NOT EXISTS (SELECT 1 FROM Post post WHERE post.profile = p)")  // Profile has no post
    long countProfilesWithCommentWithoutPosts();


    @Query("SELECT p FROM Profile p WHERE p.deleted = false")
    List<Profile> findAllActiveProfiles();

    @Query("SELECT f FROM Profile p JOIN p.following f WHERE p.id = :profileId AND p.deleted = false")
    List<Profile> findFollowingProfiles(@Param("profileId") Integer profileId);

    @Query("SELECT p FROM Profile p WHERE p.deleted = false AND p.role = :role")
    Page<Profile> findAllActiveProfiles(@Param("role") Role role, Pageable pageable);


    @Query("SELECT p FROM Profile p WHERE p.deleted = false AND p.role = :role AND p.id IN :profileIds")
    List<Profile> findAllActiveProfilesSorted(@Param("role") Role role, @Param("profileIds") List<Integer> profileIds);

    @Query("SELECT p FROM Profile p WHERE p.email = :email AND p.deleted = false")
    Profile findActiveProfileByEmail(@Param("email") String email);

    // Fetch following profiles for a specific profile with pagination
    @Query("SELECT f FROM Profile p JOIN p.following f WHERE p.id = :profileId AND p.deleted = false")
    Page<Profile> findFollowingProfiles(@Param("profileId") Integer profileId, Pageable pageable);

    @Query("SELECT p FROM Profile p WHERE p.username = :username AND p.deleted = false")
    Profile findActiveProfileByUsername(@Param("username") String username);

    @Query("SELECT MAX(p.id) FROM Profile p")
    Integer findMaxId();

    @Query("SELECT p FROM Profile p WHERE p.activated = false AND p.registrationTime < :cutoffDate")
    List<Profile> findUnactivatedProfilesBefore(@Param("cutoffDate") LocalDateTime cutoffDate);

    @Modifying//ili prodje sve ili nista
    @Query(value = "INSERT INTO profile_following (profile_id, followed_profile_id) VALUES (:profileId, :followedProfileId)", nativeQuery = true)
    void followProfile(@Param("profileId") Integer profileId, @Param("followedProfileId") Integer followedProfileId);

    @Modifying// Ensures either the entire operation succeeds or none of it does
    @Query(value = "DELETE FROM profile_following WHERE profile_id = :profileId AND followed_profile_id = :followedProfileId", nativeQuery = true)
    void unfollowProfile(@Param("profileId") Integer profileId, @Param("followedProfileId") Integer followedProfileId);


    @Query(value = "SELECT p.* FROM profile p " +
            "JOIN profile_following pf ON p.id = pf.followed_profile_id " +
            "WHERE pf.profile_id = :profileId", nativeQuery = true)
    List<Profile> getFollowers(@Param("profileId") Integer profileId);








}
