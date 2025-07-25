package rs.ac.uns.ftn.informatika.jpa.dto;

import rs.ac.uns.ftn.informatika.jpa.model.Profile;

public class ProfileTrendDTO {
    public Long likeCount;
    public String name;
    public  String surname;
    public String username;
    public Integer id;

    public ProfileTrendDTO(Profile profile, Long LikeCount) {
        name = profile.getName();
        surname = profile.getSurname();
        username = profile.getUsername();
        likeCount = LikeCount;
        id = profile.getId();
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long LikeCount) {
        likeCount = LikeCount;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
