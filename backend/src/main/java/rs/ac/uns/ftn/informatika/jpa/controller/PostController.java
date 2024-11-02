package rs.ac.uns.ftn.informatika.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.informatika.jpa.dto.PostDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.primer.StudentDTO;
import rs.ac.uns.ftn.informatika.jpa.mapper.CommentDTOMapper;
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
            postDTO.setComments(commentService.findAllForPost(post.getId())
                    .stream()
                    .map(CommentDTOMapper::fromCommentToDTO) // Use the mapping method
                    .collect(Collectors.toList()));
            postDTOs.add(postDTO);

        }

        return new ResponseEntity<>(postDTOs, HttpStatus.OK);
    }


}
