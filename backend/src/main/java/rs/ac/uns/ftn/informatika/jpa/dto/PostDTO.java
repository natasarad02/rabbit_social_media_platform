package rs.ac.uns.ftn.informatika.jpa.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PostDTO {
    private Integer id;
    private String description;
    private String picture;
    private LocalDateTime postedTime;
    private int likeCount;
    private List<CommentDTO> comments;

    public PostDTO() {}

    public PostDTO(Integer id, String description, String picture, LocalDateTime postedTime, int likeCount, List<CommentDTO> comments) {
        this.id = id;
        this.description = description;
        this.picture = picture;
        this.postedTime = postedTime;
        this.likeCount = likeCount;
        this.comments = comments;
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
}
