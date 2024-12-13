package rs.ac.uns.ftn.informatika.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.jpa.dto.CommentDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.PostDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.ProfileDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.primer.StudentDTO;
import rs.ac.uns.ftn.informatika.jpa.mapper.CommentDTOMapper;
import rs.ac.uns.ftn.informatika.jpa.mapper.PostDTOMapper;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;
import rs.ac.uns.ftn.informatika.jpa.model.primer.Student;
import rs.ac.uns.ftn.informatika.jpa.service.CommentService;
import rs.ac.uns.ftn.informatika.jpa.service.ImageService;
import rs.ac.uns.ftn.informatika.jpa.service.PostService;
import rs.ac.uns.ftn.informatika.jpa.service.ProfileService;

import javax.annotation.security.PermitAll;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/posts")
public class PostController {
    private PostService postService;
    private CommentService commentService;
    private ProfileService profileService;
    private ImageService imageService;

    private PostDTOMapper postDTOMapper;
    public PostController(@Autowired PostService postService, @Autowired CommentService commentService, @Autowired ProfileService profileService, @Autowired ImageService imageService ) {
        this.postService = postService;
        this.commentService = commentService;
        this.profileService = profileService;
        this.imageService = imageService;
    }

    @GetMapping(value = "/forProfile/{id}")
    @PreAuthorize("hasAnyAuthority('User')")
    public ResponseEntity<List<PostDTO>> getAllPostsForProfile(@PathVariable Integer id) {

        List<Post> posts = postService.findByProfileId(id);

        posts.sort((p1, p2) -> p2.getPostedTime().compareTo(p1.getPostedTime()));

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
                commentDTOs.add(new CommentDTO(comment));
            }
            postDTO.setComments(commentDTOs);
            postDTO.setAddress(post.getAddress());
            postDTO.setLongitude(post.getLongitude());
            postDTO.setLatitude(post.getLatitude());
            postDTO.setProfile(new ProfileDTO(post.getProfile()));
            postDTOs.add(postDTO);

        }

        return new ResponseEntity<>(postDTOs, HttpStatus.OK);
    }



    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts() {

        List<Post> posts = postService.findAllActive();

        posts.sort((p1, p2) -> p2.getPostedTime().compareTo(p1.getPostedTime()));

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
                commentDTOs.add(new CommentDTO(comment));
            }
            postDTO.setComments(commentDTOs);
            postDTO.setAddress(post.getAddress());
            postDTO.setLongitude(post.getLongitude());
            postDTO.setLatitude(post.getLatitude());
            postDTO.setProfile(new ProfileDTO(post.getProfile()));
            postDTOs.add(postDTO);

        }

        return new ResponseEntity<>(postDTOs, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(value = "/id/{id}")
    @PreAuthorize("hasAnyAuthority('Administrator', 'User')")
    public ResponseEntity<PostDTO> getPostById(@PathVariable("id") Integer id) {

        Post post = postService.findOne(id);
        PostDTO postDTO = new PostDTO(post);

        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @PutMapping(consumes = "application/json")
    @PreAuthorize("hasAuthority('User')")
    public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO postDTO) throws IOException {

        Post updatedPost = postService.updatePost(postDTO);

        if (updatedPost == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new PostDTO(updatedPost), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('Administrator', 'User')")
    @PutMapping("/delete/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<PostDTO> deletePost(@PathVariable Integer id) {

        Post post = postService.deletePost(id);
        return new ResponseEntity<>(new PostDTO(post), HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('User')")
    @PostMapping("/createpost/{profileId}")
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO, @PathVariable Integer profileId) throws IOException {
        Post post = postDTOMapper.fromDTOtoPost(postDTO);
        Profile profile = profileService.findOne(profileId);
        String imagePath = imageService.saveImage(postDTO.getImageBase64());
        post.setPicture(imagePath);
        post.setProfile(profile);
        if(post == null)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        post = postService.save(post);
        return new ResponseEntity<>(new PostDTO(post), HttpStatus.OK);

    }


    @PreAuthorize("hasAuthority('User')")
    @PostMapping("/like")
    public ResponseEntity<Void> likePost(@RequestParam Integer profileId, @RequestParam Integer postId) {
        postService.addLike(profileId, postId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('User')")
    @PostMapping("/dislike")
    public ResponseEntity<Void> dislikePost(@RequestParam Integer profileId, @RequestParam Integer postId) {
        postService.removeLike(profileId, postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/likes")
    public ResponseEntity<List<Integer>> getAllLikes(@RequestParam Integer profileId) {
        List<Integer> likes = postService.getPostIdsForProfile(profileId);
        return new ResponseEntity<>(likes, HttpStatus.OK);
    }


}
