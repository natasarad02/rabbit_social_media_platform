package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.repository.CommentRepository;

import java.util.List;

@Service
public class CommentService {
    private CommentRepository commentRepository;

    public CommentService(@Autowired CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> findAllActive() {
        return commentRepository.findAllComments();
    }

    public List<Comment> findAllForPost(Integer postId) {
        return commentRepository.findAllCommentsByPostId(postId);
    }
}
