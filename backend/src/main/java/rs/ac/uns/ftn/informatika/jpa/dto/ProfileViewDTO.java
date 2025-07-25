package rs.ac.uns.ftn.informatika.jpa.dto;

import rs.ac.uns.ftn.informatika.jpa.model.Role;

public class ProfileViewDTO {
    private Integer id;
    private String name;
    private String email;
    private String username;
    private String surname;
    private Role role;
    private int postCount;
    private int followingCount;

    public ProfileViewDTO() {}

    public ProfileViewDTO(Integer id, String name, String email, Role role, String username, String surname, int postCount, int followingCount) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.surname = surname;
        this.username = username;
        this.role = role;
        this.postCount = postCount;
        this.followingCount = followingCount;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
