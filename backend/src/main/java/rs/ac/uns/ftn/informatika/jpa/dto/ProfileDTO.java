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
    private Timestamp lastActiveDate;
    private String address;
    private boolean isActive;
    public ProfileDTO() {}

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
        this.lastActiveDate = profile.getLastActiveDate();
        this.isActive = profile.isActive();
    }

    public ProfileDTO(Integer id, String name, String surname, String email, String username, Role role,
                      boolean deleted, Timestamp lastPasswordResetDate, String address, Timestamp lastActiveDate, boolean isActive) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.username = username;
        this.role = role;
        this.deleted = deleted;
        this.lastPasswordResetDate = lastPasswordResetDate;
        this.address = address;
        this.lastActiveDate = lastActiveDate;
        this.isActive = isActive;
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

    public Timestamp getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(Timestamp lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    public String getUsername() {
        return username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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

}
