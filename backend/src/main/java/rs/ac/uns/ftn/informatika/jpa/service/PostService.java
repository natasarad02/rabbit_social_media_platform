package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.dto.PostDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.ProfileTrendDTO;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;
import rs.ac.uns.ftn.informatika.jpa.model.primer.Student;
import rs.ac.uns.ftn.informatika.jpa.repository.PostRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.ProfileRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.primer.StudentRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private PostRepository postRepository;
    private ProfileRepository profileRepository;
    private ImageService imageService;

    public PostService(@Autowired PostRepository postRepository, @Autowired ProfileRepository profileRepository, @Autowired ImageService imageService) {
        this.postRepository = postRepository;
        this.profileRepository = profileRepository;
        this.imageService = imageService;
    }

    public Page<Post> findAllByProfileId(Pageable page, Integer profileId) {
        return postRepository.findAllByProfileId(profileId, page);
    }

    public Post findOne(Integer id) {
        return postRepository.findById(id).orElseGet(null);
    }

    public List<Post> findAllActive() {
        return postRepository.findAll();
    }

    public Integer countPostsForProfile(Integer profileId) {
        return postRepository.countPostsForProfile(profileId);
    }

    public Integer countLikesForPost(Integer postId) {
        return postRepository.countLikesForPost(postId);
    }

    @Transactional
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Transactional
    public Post deletePost(Integer id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post != null && !post.isDeleted()) {
            post.setDeleted(true);
            return postRepository.save(post);
        }
        return null;
    }

    @Transactional
    public Post updatePost(PostDTO postDTO) throws IOException {

        Post post = findOne(postDTO.getId());

        if (post == null) {
            return null;
        }

        post.setDescription(postDTO.getDescription());

        if(postDTO.getImageBase64() != null && !postDTO.getImageBase64().isEmpty()) {
            String imagePath = imageService.saveImage(postDTO.getImageBase64());
            post.setPicture(imagePath);
        }
        post.setAddress(postDTO.getAddress());
        post.setLongitude(postDTO.getLongitude());
        post.setLatitude(postDTO.getLatitude());

        System.out.println(post);

        return postRepository.save(post);
    }

    public List<Post> findByProfileId(Integer profileId) {
        return postRepository.findAllByProfileId(profileId);
    }

   public void remove(Integer id) {
        postRepository.deleteById(id);
    }



    public Post updatePost(Integer id, Post updatedPost) {
        Optional<Post> existingPostOpt = postRepository.findById(id);

        if (existingPostOpt.isPresent()) {
            Post existingPost = existingPostOpt.get();
            existingPost.setPicture(updatedPost.getPicture());
            existingPost.setDeleted(updatedPost.isDeleted());
            existingPost.setDescription(updatedPost.getDescription());
            existingPost.setAddress(updatedPost.getAddress());
            existingPost.setLongitude(updatedPost.getLongitude());
            existingPost.setLatitude(updatedPost.getLatitude());

            return postRepository.save(existingPost);
        } else {
            throw new EntityNotFoundException("Post with id " + id + " not found.");
        }
    }

    public void addLike(Integer profileId, Integer postId) {
        postRepository.addLike(profileId, postId);
    }

    public List<Integer> getPostIdsForProfile(Integer profileId) {
        return postRepository.findLikedPostIdsByProfileId(profileId);
    }

    public  Post getById(Integer id) {
        return postRepository.findById(id).orElse(null);
    }

    public int getNumberOfPosts(){
        return postRepository.getTotalNumberOfPosts();
    }

    public int getNumberOfPostsInLastMonth(){
        LocalDateTime lastMonth = LocalDateTime.now().minusDays(30);
        int postsInLastMonth = postRepository.getNumberOfPostsInLastMonth(lastMonth);
        return postsInLastMonth;
    }

    public List<Post> findTop5MostLikedPostsInLast7Days() {
        LocalDateTime lastWeek = LocalDateTime.now().minusDays(7);
        Pageable topFive = PageRequest.of(0, 5);
        List<Post> topPostsLast7Days = postRepository.findTop5MostLikedPostsInLast7Days(lastWeek, topFive);
        return topPostsLast7Days;
    }

    public  List<Post> getTop10MostLikedPosts(){
        Pageable topTen = PageRequest.of(0, 10);
        List<Post> topLikedPosts = postRepository.getTop10MostLikedPosts(topTen);
        return topLikedPosts;
    }

    public List<ProfileTrendDTO> findProfilesWithMostLikesGivenInLast7Days(){
        LocalDateTime lastWeek = LocalDateTime.now().minusDays(7);
        List<Object[]> topProfilesData = postRepository.findTopProfileIdsByLikesGivenInLast7Days(lastWeek);

        List<ProfileTrendDTO> profileTrendDTOs = new ArrayList<>();
        for (Object[] data : topProfilesData) {
            Integer profileId = (Integer) data[0];
            Profile profile = profileRepository.findById(profileId)
                    .orElseThrow(() -> new RuntimeException("Profile not found: " + profileId));

            Long likeCount = ((BigInteger) data[1]).longValue();
            // System.out.println("Profile ID: " + profileId + ", Like Count: " + likeCount);
            profileTrendDTOs.add(new ProfileTrendDTO(profile, likeCount));
        }
        return profileTrendDTOs;
    }
}
