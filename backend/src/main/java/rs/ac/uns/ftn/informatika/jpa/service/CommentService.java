package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.repository.CommentRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {
    private CommentRepository commentRepository;

    public CommentService(@Autowired CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment addComment(Comment comment) {
        if(isCommentNumberLessThanLimit(comment)) {
            return commentRepository.save(comment);
        }
       return null;
    }

    private boolean isCommentNumberLessThanLimit(Comment comment) {
        List<Comment> allCommentsByProfile = commentRepository.findAllCommentsByProfileId(comment.getProfile().getId());
        Integer count = 0;
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        for(Comment c : allCommentsByProfile) {
            if(c.getCommentedTime().isAfter(oneHourAgo)) {
                count++;

            }
        }

        if(count < 61)
            return true;

        return false;



    }

    public List<Comment> findAllActive() {
        return commentRepository.findAllComments();
    }

    public List<Comment> findAllForPost(Integer postId) {
        return commentRepository.findAllCommentsByPostId(postId);
    }
}
