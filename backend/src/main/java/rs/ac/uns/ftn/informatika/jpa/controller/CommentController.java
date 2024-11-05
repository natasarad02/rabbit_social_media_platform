package rs.ac.uns.ftn.informatika.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.jpa.dto.CommentDTO;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;
import rs.ac.uns.ftn.informatika.jpa.service.CommentService;
import rs.ac.uns.ftn.informatika.jpa.service.PostService;
import rs.ac.uns.ftn.informatika.jpa.service.ProfileService;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "api/comments")
public class CommentController {
    private CommentService commentService;
    private ProfileService profileService;
    private PostService postService;

    public CommentController(@Autowired CommentService commentService , @Autowired ProfileService profileService , @Autowired PostService postService) {
        this.commentService = commentService;
        this.profileService = profileService;
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(
            @RequestParam Integer idPost,
            @RequestParam Integer idProfile,
            @RequestBody CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setText(commentDTO.getText());
        comment.setCommentedTime(LocalDateTime.now());
        Post post = postService.findOne(idPost);
        comment.setPost(post);
        comment.setDeleted(false);

        Profile profile = profileService.findOne(idProfile);
        comment.setProfile(profile);

        Comment savedComment = commentService.addComment(comment);
        CommentDTO c = new CommentDTO(savedComment);
        return ResponseEntity.status(HttpStatus.CREATED).body(c);
    }
}
