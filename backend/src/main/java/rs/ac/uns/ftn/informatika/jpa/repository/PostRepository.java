package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Query("SELECT COUNT(p) FROM Post p WHERE p.profile.id != :profileId AND p.postedTime >= :sevenDaysAgo AND p.deleted = false")
    int countPostsInLastSevenDays(@Param("profileId") Integer profileId, @Param("sevenDaysAgo") LocalDateTime sevenDaysAgo);

    @Modifying
    @Query(value = "INSERT INTO likes (profile_id, post_id, like_time) VALUES (:profileId, :postId, CURRENT_TIMESTAMP)", nativeQuery = true)
    void addLike(@Param("profileId") Integer profileId, @Param("postId") Integer postId);

    @Query("SELECT p FROM Post p WHERE p.id = :postId")
    Optional<Post> findByIdForUpdate(@Param("postId") Integer postId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM likes WHERE profile_id = :profileId AND post_id = :postId", nativeQuery = true)
    void removeLike(@Param("profileId") Integer profileId, @Param("postId") Integer postId);



    @Query(value = "SELECT post_id FROM likes WHERE profile_id = :profileId", nativeQuery = true)
    List<Integer> findLikedPostIdsByProfileId(@Param("profileId") Integer profileId);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.deleted = false")
    Integer getTotalNumberOfPosts();

    @Query("SELECT COUNT(p) FROM Post p WHERE p.postedTime >= :lastMonth")
    Integer getNumberOfPostsInLastMonth(@Param("lastMonth") LocalDateTime lastMonth);

    @Query("SELECT p FROM Post p LEFT JOIN p.likedPosts l WHERE p.postedTime >= :lastWeek GROUP BY p.id ORDER BY COUNT(l) DESC")
    List<Post> findTop5MostLikedPostsInLast7Days(@Param("lastWeek") LocalDateTime lastWeek, Pageable pageable);

    @Query("SELECT p FROM Post p LEFT JOIN p.likedPosts l GROUP BY p.id ORDER BY COUNT(l) DESC")
    List<Post> getTop10MostLikedPosts(Pageable pageable);

    @Query(value = "SELECT l.profile_id, COUNT(l.post_id) AS likeCount FROM likes l JOIN post p ON l.post_id = p.id WHERE l.like_time >= :lastWeek AND p.deleted = false GROUP BY l.profile_id ORDER BY likeCount DESC", nativeQuery = true)
    List<Object[]> findTopProfileIdsByLikesGivenInLast7Days(@Param("lastWeek") LocalDateTime lastWeek);



}
