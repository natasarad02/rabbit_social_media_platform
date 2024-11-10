package rs.ac.uns.ftn.informatika.jpa.dto;

import rs.ac.uns.ftn.informatika.jpa.model.Location;
import rs.ac.uns.ftn.informatika.jpa.model.Post;

import java.time.LocalDateTime;
import java.util.List;

public class PostDTO {
    private Integer id;
    private String description;
    private String picture;
    private LocalDateTime postedTime;
    private boolean deleted;
    private int likeCount;
    private List<CommentDTO> comments;
   // private LocationDTO location;
 //   private ProfileDTO profile;
    private String address;
    private double longitude;
    private double latitude;

    private ProfileDTO profile;

    public PostDTO() {}

    public PostDTO(Post post) {
        this.id = post.getId();
        this.description = post.getDescription();
        this.picture = post.getPicture();
        this.postedTime = post.getPostedTime();
        this.deleted = post.isDeleted();
        this.address = post.getAddress();
        this.longitude = post.getLongitude();
        this.latitude = post.getLatitude();
        this.profile = new ProfileDTO(post.getProfile());
       // this.location = new LocationDTO(post.getLocation());
       // this.profile = new ProfileDTO(post.getProfile());



    }

    public PostDTO(Integer id, String description, String picture, LocalDateTime postedTime, int likeCount, List<CommentDTO> comments, String address, double longitude, double latitude, ProfileDTO profile) {
        this.id = id;
        this.description = description;
        this.picture = picture;
        this.postedTime = postedTime;
        this.likeCount = likeCount;
        this.comments = comments;
        this.deleted = false;
      //  this.location = location;
      //  this.profile = profile;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.profile = profile;

    }

    public ProfileDTO getProfile() {
        return profile;
    }

    public void setProfile(ProfileDTO profile) {
        this.profile = profile;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public LocalDateTime getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(LocalDateTime postedTime) {
        this.postedTime = postedTime;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
