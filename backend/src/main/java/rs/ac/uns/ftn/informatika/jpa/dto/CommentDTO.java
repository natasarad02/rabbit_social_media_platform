package rs.ac.uns.ftn.informatika.jpa.dto;

import rs.ac.uns.ftn.informatika.jpa.model.Comment;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;

import java.time.LocalDateTime;

public class CommentDTO {
    private Integer id;
    private String text;
    private LocalDateTime commentedTime;
    private ProfileDTO profile;

    public CommentDTO() {}

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.text = comment.getText();
        this.commentedTime = comment.getCommentedTime();
        this.profile = new ProfileDTO(comment.getProfile());
    }

    public CommentDTO(Integer id, String text, Profile profile, LocalDateTime commentedTime) {
        this.id = id;
        this.text = text;
        this.commentedTime = commentedTime;
        this.profile = new ProfileDTO(profile);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ProfileDTO getProfile() {
        return profile;
    }

    public void setProfile(ProfileDTO profile) {
        this.profile = profile;
    }

    public LocalDateTime getCommentedTime() {
        return commentedTime;
    }

    public void setCommentedTime(LocalDateTime commentedTime) {
        this.commentedTime = commentedTime;
    }
}
