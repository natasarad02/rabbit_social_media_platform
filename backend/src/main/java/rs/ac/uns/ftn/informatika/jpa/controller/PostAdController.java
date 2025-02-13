package rs.ac.uns.ftn.informatika.jpa.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.informatika.jpa.dto.PostAdDTO;
import rs.ac.uns.ftn.informatika.jpa.service.PostPublisherService;

@RestController
@RequestMapping(value = "api/post_ads")
public class PostAdController {
    private final PostPublisherService postPublisher;

    public PostAdController(PostPublisherService postPublisher) {
        this.postPublisher = postPublisher;
    }

    @PostMapping("/send_ad")
    public ResponseEntity<PostAdDTO> sendPost(@RequestBody PostAdDTO post) throws JsonProcessingException {
        postPublisher.sendPostForAds(convertToJson(post));
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    private String convertToJson(PostAdDTO postAdDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(postAdDTO);
    }
}
