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
import rs.ac.uns.ftn.informatika.jpa.service.RateLimiterService;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "api/comments")
public class CommentController {
    private CommentService commentService;
    private ProfileService profileService;
    private PostService postService;
    private final RateLimiterService rateLimiterService;

    public CommentController(@Autowired CommentService commentService , @Autowired ProfileService profileService , @Autowired PostService postService, @Autowired RateLimiterService rateLimiterService) {
        this.commentService = commentService;
        this.profileService = profileService;
        this.postService = postService;
        this.rateLimiterService = rateLimiterService;
    }

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(
            @RequestParam Integer idPost,
            @RequestParam Integer idProfile,
            @RequestBody CommentDTO commentDTO) {

        if (!rateLimiterService.isAllowed(idProfile)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(commentDTO);
        }

        Comment comment = new Comment();
        comment.setText(commentDTO.getText());
        comment.setCommentedTime(LocalDateTime.now());
        Post post = postService.findOne(idPost);
        if (post == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commentDTO);
        }
        comment.setPost(post);
        comment.setDeleted(false);

        Profile profile = profileService.findOne(idProfile);
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commentDTO);
        }
        comment.setProfile(profile);

        Comment savedComment = commentService.addComment(comment);

        if(savedComment == null)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(commentDTO);
        }
        CommentDTO c = new CommentDTO(savedComment);
        return ResponseEntity.status(HttpStatus.CREATED).body(c);
    }
}
