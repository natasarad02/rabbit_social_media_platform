package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    Integer countAllByPostedTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT p FROM Post p WHERE p.profile.id = :profileId AND p.deleted = false")
    List<Post> findAllByProfileId(@Param("profileId") Integer profileId);

    @Query("SELECT p FROM Post p WHERE p.profile.id = :profileId AND p.deleted = false")
    Page<Post> findAllByProfileId(@Param("profileId") Integer profileId, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.profile.id = :profileId AND p.deleted = false")
    Integer countPostsForProfile(@Param("profileId") Integer profileId);

    @Query("SELECT COUNT(l) FROM Post p JOIN p.likedPosts l WHERE p.id = :postId")
    Integer countLikesForPost(@Param("postId") Integer postId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO likes (profile_id, post_id) VALUES (:profileId, :postId)", nativeQuery = true)
    void addLike(@Param("profileId") Integer profileId, @Param("postId") Integer postId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM likes WHERE profile_id = :profileId AND post_id = :postId", nativeQuery = true)
    void removeLike(@Param("profileId") Integer profileId, @Param("postId") Integer postId);


    @Query(value = "SELECT post_id FROM likes WHERE profile_id = :profileId", nativeQuery = true)
    List<Integer> findLikedPostIdsByProfileId(@Param("profileId") Integer profileId);

}
