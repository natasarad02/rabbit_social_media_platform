package rs.ac.uns.ftn.informatika.jpa.dto;

import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;

import java.time.LocalDateTime;

public class CommentDTO {
    private Integer id;
    private String text;
    private LocalDateTime commentedTime;

    public CommentDTO() {}

    public CommentDTO(Integer id, String text, LocalDateTime commentedTime) {
        this.id = id;
        this.text = text;
        this.commentedTime = commentedTime;
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

    public LocalDateTime getCommentedTime() {
        return commentedTime;
    }

    public void setCommentedTime(LocalDateTime commentedTime) {
        this.commentedTime = commentedTime;
    }
}
