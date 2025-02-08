package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;
import rs.ac.uns.ftn.informatika.jpa.model.Post;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Integer countAllByCommentedTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT c from Comment c where c.deleted = false ")
    List<Comment> findAllComments();

    @Query("SELECT c FROM Comment c WHERE c.deleted = false AND c.post.id = :postId")
    List<Comment> findAllCommentsByPostId(@Param("postId") Integer postId);

    @Query("SELECT c FROM Comment c WHERE c.deleted = false AND c.profile.id = :profileId")
    List<Comment> findAllCommentsByProfileId(@Param("profileId") Integer postId);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId AND c.commentedTime >= :sevenDaysAgo AND c.deleted = false")
    int countCommentsInLastSevenDays(@Param("postId") Integer postId, @Param("sevenDaysAgo") LocalDateTime sevenDaysAgo);
}
