package rs.ac.uns.ftn.informatika.jpa.model;

import java.time.LocalDateTime;

import javax.persistence.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/* primer logickog brisanja
 *
 * Prilikom poziva delete() metode repozitorijuma, okidace se ovaj upit koji radi soft delete
 * tako sto menja status deleted polja sa false na true.
 */
@SQLDelete(sql
        = "UPDATE comment "
        + "SET deleted = true "
        + "WHERE id = ?")
@Where(clause = "deleted = false")
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "text", nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "commented_time", nullable = false)
    private LocalDateTime commentedTime;

    @PrePersist
    protected void onCreate() {
        if (commentedTime == null) {
            commentedTime = LocalDateTime.now();
        } else {
            validatePostedTime();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        validatePostedTime();
    }

    private void validatePostedTime() {
        if (commentedTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Posted time cannot be in the future.");
        }
    }

    @Column(name = "deleted")
    private boolean deleted;



    public Comment() {

    }

    public Comment(String text, Integer id, Profile profile, Post post, LocalDateTime commentedTime, boolean deleted) {
        this.text = text;
        this.id = id;
        this.profile = profile;
        this.post = post;
        this.commentedTime = commentedTime;
        this.deleted = deleted;
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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Comment t = (Comment) o;
        return id != null && id.equals(t.getId());
    }

    @Override
    public int hashCode() {
        return 1337;
    }


}
