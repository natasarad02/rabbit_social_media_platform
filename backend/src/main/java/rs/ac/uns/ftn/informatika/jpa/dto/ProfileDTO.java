package rs.ac.uns.ftn.informatika.jpa.dto;

import rs.ac.uns.ftn.informatika.jpa.model.Profile;
import rs.ac.uns.ftn.informatika.jpa.model.Role;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;
import rs.ac.uns.ftn.informatika.jpa.model.Post;

import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

public class ProfileDTO {
    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String username;
    private Role role;
    private boolean deleted;
    private Timestamp lastPasswordResetDate;
    private String address;
    private Set<Integer> followingIds;
    private Set<Integer> followerIds;
    private Set<Integer> commentIds;
    private Set<Integer> postIds;
    private Set<Integer> likedPostIds;

    public ProfileDTO(Profile profile) {
        this.id = profile.getId();
        this.name = profile.getName();
        this.surname = profile.getSurname();
        this.email = profile.getEmail();
        this.username = profile.getUsername();
        this.role = profile.getRole();
        this.deleted = profile.isDeleted();
        this.lastPasswordResetDate = profile.getLastPasswordResetDate();
        this.address = profile.getAddress();
        this.followingIds = profile.getFollowing().stream().map(Profile::getId).collect(Collectors.toSet());
        this.followerIds = profile.getFollowers().stream().map(Profile::getId).collect(Collectors.toSet());
        this.commentIds = profile.getComments().stream().map(Comment::getId).collect(Collectors.toSet());
        this.postIds = profile.getPosts().stream().map(Post::getId).collect(Collectors.toSet());
        this.likedPostIds = profile.getLikedPosts().stream().map(Post::getId).collect(Collectors.toSet());
    }

    public ProfileDTO(Integer id, String name, String surname, String email, String username, Role role,
                      boolean deleted, Timestamp lastPasswordResetDate, String address, Set<Integer> followingIds,
                      Set<Integer> followerIds, Set<Integer> commentIds, Set<Integer> postIds, Set<Integer> likedPostIds) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.username = username;
        this.role = role;
        this.deleted = deleted;
        this.lastPasswordResetDate = lastPasswordResetDate;
        this.address = address;
        this.followingIds = followingIds;
        this.followerIds = followerIds;
        this.commentIds = commentIds;
        this.postIds = postIds;
        this.likedPostIds = likedPostIds;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Timestamp getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public String getAddress() {
        return address;
    }

    public Set<Integer> getFollowingIds() {
        return followingIds;
    }

    public Set<Integer> getFollowerIds() {
        return followerIds;
    }

    public Set<Integer> getCommentIds() {
        return commentIds;
    }

    public Set<Integer> getPostIds() {
        return postIds;
    }

    public Set<Integer> getLikedPostIds() {
        return likedPostIds;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setLastPasswordResetDate(Timestamp lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setFollowingIds(Set<Integer> followingIds) {
        this.followingIds = followingIds;
    }

    public void setFollowerIds(Set<Integer> followerIds) {
        this.followerIds = followerIds;
    }

    public void setCommentIds(Set<Integer> commentIds) {
        this.commentIds = commentIds;
    }

    public void setPostIds(Set<Integer> postIds) {
        this.postIds = postIds;
    }

    public void setLikedPostIds(Set<Integer> likedPostIds) {
        this.likedPostIds = likedPostIds;
    }
}
