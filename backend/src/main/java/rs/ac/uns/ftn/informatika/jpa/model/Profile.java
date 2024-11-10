package rs.ac.uns.ftn.informatika.jpa.model;



import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;



@SQLDelete(sql
        = "UPDATE profile "
        + "SET deleted = true "
        + "WHERE id = ?")
@Where(clause = "deleted = false")
@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Enumerated(EnumType.STRING) // or EnumType.ORDINAL, depending on your preference
    @Column(name = "role")
    private Role role;

    @Column(name = "deleted")
    private boolean deleted;

    @Column(name = "address", nullable = false)
    private String address;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    @JoinTable(
            name = "profile_following",
            joinColumns = @JoinColumn(name = "profile_id" , referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "followed_profile_id" , referencedColumnName = "id")
    )
    private Set<Profile> following = new HashSet<>();

    @ManyToMany(mappedBy = "following")
    private Set<Profile> followers = new HashSet<>();


    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Post> posts = new HashSet<Post>();

    /*@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location address;*/

    // Set of users that follow this user


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.DETACH})
    @JoinTable(name = "likes",
            joinColumns = @JoinColumn(name = "profile_id" , referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "post_id" , referencedColumnName = "id"))
    private Set<Post> likedPosts = new HashSet<>();


    public Profile() {

    }

    public String getAddress() {
        return address;
    }

    public Profile(Integer id, String email, String password, String name, String surname, Role role, boolean deleted, String address) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.deleted = deleted;
        this.address = address;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<Profile> getFollowing() {
        return following;
    }

    public void setFollowing(Set<Profile> following) {
        this.following = following;
    }

    public Set<Profile> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<Profile> followers) {
        this.followers = followers;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setProfile(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setProfile(this);
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public void addPost(Post post) {
        posts.add(post);
        post.setProfile(this);
    }

    public void removePost(Post post) {
        posts.remove(post);
        post.setProfile(this);
    }



    public Set<Post> getLikedPosts() {
        return likedPosts;
    }

    public void setLikedPosts(Set<Post> likedPosts) {
        this.likedPosts = likedPosts;
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
        Profile t = (Profile) o;
        return id != null && id.equals(t.getId());
    }

    @Override
    public int hashCode() {
        return 1337;
    }

}


