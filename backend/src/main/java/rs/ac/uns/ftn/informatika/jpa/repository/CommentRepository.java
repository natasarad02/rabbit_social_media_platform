package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;
import rs.ac.uns.ftn.informatika.jpa.model.Post;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("SELECT c from Comment c where c.deleted = false ")
    List<Comment> findAllComments();

    @Query("SELECT c FROM Comment c WHERE c.deleted = false AND c.post.id = :postId")
    List<Comment> findAllCommentsByPostId(@Param("postId") Integer postId);
}
