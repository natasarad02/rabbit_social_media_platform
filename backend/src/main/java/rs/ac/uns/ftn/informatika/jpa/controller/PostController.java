package rs.ac.uns.ftn.informatika.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.jpa.dto.CommentDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.PostDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.primer.StudentDTO;
import rs.ac.uns.ftn.informatika.jpa.mapper.CommentDTOMapper;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;
import rs.ac.uns.ftn.informatika.jpa.model.primer.Student;
import rs.ac.uns.ftn.informatika.jpa.service.CommentService;
import rs.ac.uns.ftn.informatika.jpa.service.PostService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/posts")
public class PostController {
    private PostService postService;
    private CommentService commentService;

    public PostController(@Autowired PostService postService, @Autowired CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping(value = "/forProfile/{id}")
    public ResponseEntity<List<PostDTO>> getAllPostsForProfile(@PathVariable Integer id) {

        List<Post> posts = postService.findByProfileId(id);

        // convert students to DTOs
        List<PostDTO> postDTOs = new ArrayList<>();
        for (Post post : posts) {
            PostDTO postDTO = new PostDTO();
            postDTO.setId(post.getId());
            postDTO.setPostedTime(post.getPostedTime());
            postDTO.setPicture(post.getPicture());
            postDTO.setDescription(post.getDescription());
            postDTO.setLikeCount(postService.countLikesForPost(post.getId()));
            List<CommentDTO> commentDTOs = new ArrayList<>();
            for (Comment comment : commentService.findAllForPost(post.getId())) {
                commentDTOs.add(new CommentDTO(comment)); // Use the constructor directly
            }
            postDTO.setComments(commentDTOs);
            postDTOs.add(postDTO);

        }

        return new ResponseEntity<>(postDTOs, HttpStatus.OK);
    }

    @PutMapping(consumes = "application/json")
    public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO postDTO) {

        Post post = postService.findOne(postDTO.getId());

        if (post == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        post.setDescription(postDTO.getDescription());
        post.setPicture(postDTO.getPicture());

        post = postService.save(post);
        return new ResponseEntity<>(new PostDTO(post), HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<PostDTO> deletePost(@PathVariable Integer id) {

        Post post = postService.findOne(id);

        if (post == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        post.setDeleted(true);

        post = postService.save(post);
        return new ResponseEntity<>(new PostDTO(post), HttpStatus.OK);
    }

   



}
