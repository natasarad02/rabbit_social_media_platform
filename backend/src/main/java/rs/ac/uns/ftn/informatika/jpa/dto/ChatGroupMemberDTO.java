package rs.ac.uns.ftn.informatika.jpa.dto;

import rs.ac.uns.ftn.informatika.jpa.model.ChatGroup;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;

import java.time.LocalDateTime;

public class ChatGroupMemberDTO {
    private Integer id;
    private Integer chatGroupId;
    private Integer profileId;
    private LocalDateTime joinDate;

    public ChatGroupMemberDTO() {}

    public ChatGroupMemberDTO(Integer id, Integer chatGroupId, Integer profile, LocalDateTime joinDate) {
        this.id = id;
        this.chatGroupId = chatGroupId;
        this.profileId = profile;
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

    public Integer getProfile() {
        return profileId;
    }

    public void setProfile(Integer profile) {
        this.profileId = profile;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }
}
