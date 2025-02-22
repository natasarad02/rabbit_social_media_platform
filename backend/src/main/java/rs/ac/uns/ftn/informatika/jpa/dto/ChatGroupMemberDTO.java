package rs.ac.uns.ftn.informatika.jpa.dto;

import rs.ac.uns.ftn.informatika.jpa.model.ChatGroup;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;

import java.time.LocalDateTime;

public class ChatGroupMemberDTO {
    private Integer id;
    private Integer chatGroupId;
    private Profile profile;
    private LocalDateTime joinDate;

    public ChatGroupMemberDTO() {}

    public ChatGroupMemberDTO(Integer id, Integer chatGroupId, Profile profile, LocalDateTime joinDate) {
        this.id = id;
        this.chatGroupId = chatGroupId;
        this.profile = profile;
        this.joinDate = joinDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getChatGroupId() {
        return chatGroupId;
    }

    public void setChatGroupId(Integer chatGroupId) {
        this.chatGroupId = chatGroupId;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }
}
