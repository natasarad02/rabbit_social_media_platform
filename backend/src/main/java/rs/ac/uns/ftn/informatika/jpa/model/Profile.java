package rs.ac.uns.ftn.informatika.jpa.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import javax.persistence.*;
import javax.persistence.Version;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


@SQLDelete(sql = "UPDATE profile SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Entity
public class Profile implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "deleted")
    private boolean deleted;

    @Column(name = "activated")
    private boolean activated;

    @Column(name = "minute_following")
    private Integer minute_following;

    @Column(name = "last_follow_time")
    private LocalDateTime lastFollowTime;

    @Version
    private Integer version;

    public boolean isActivated() {
        return activated;
    }

    @Column(name = "registration_time")
    private LocalDateTime registrationTime;

    @Column(name = "last_password_reset_date")
    private Timestamp lastPasswordResetDate;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    @JoinTable(
            name = "profile_following",
            joinColumns = @JoinColumn(name = "profile_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "followed_profile_id", referencedColumnName = "id")
    )



    private Set<Profile> following = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "following")
    private Set<Profile> followers = new HashSet<>();

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Post> posts = new HashSet<>();

    @Column(name = "address", nullable = true)
    private String address;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    @JoinTable(name = "likes",
            joinColumns = @JoinColumn(name = "profile_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"))
    private Set<Post> likedPosts = new HashSet<>();

    public Profile() {
    }


    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.name())); // Using singleton for a single authority
    }


    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
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

    public Profile(Integer id, String email, String username, String password, String firstName,
                   String lastName, boolean deleted, Role role, Timestamp lastDate,
                   LocalDateTime registartion_time, boolean active, String address, int minute_following,
                   LocalDateTime last_follow_time) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.name = firstName;
        this.surname = lastName;
        this.deleted = deleted;
        this.role = role;
        this.lastPasswordResetDate = lastDate;
        this.address = address;
        this.activated = active;
        this.registrationTime = registartion_time;
        this.minute_following = minute_following;
        this.lastFollowTime = last_follow_time;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !deleted;
    }

    // Getter and Setter methods for all fields...

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        Timestamp now = new Timestamp(new Date().getTime());
        this.setLastPasswordResetDate(now);
        this.password = password;
    }

    public Timestamp getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Timestamp lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
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

    public Integer getMinute_following() {
        return minute_following;
    }

    public void setMinute_following(Integer minute_following) {
        this.minute_following = minute_following;
    }

    public void removePost(Post post) {
        posts.remove(post);
        post.setProfile(this);
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(LocalDateTime registrationTime) {
        this.registrationTime = registrationTime;
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

    public LocalDateTime getLastFollowTime() {
        return lastFollowTime;
    }

    public void setLastFollowTime(LocalDateTime lastFollowTime) {
        this.lastFollowTime = lastFollowTime;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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
