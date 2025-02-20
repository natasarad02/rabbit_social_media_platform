package rs.ac.uns.ftn.informatika.jpa.dto;

import rs.ac.uns.ftn.informatika.jpa.model.ChatMessage;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatGroupDTO {
    private Integer id;
    private String name;
    private ProfileDTO admin;
    private Set<ProfileDTO> members;
    private List<ChatMessageDTO> messages;

    public ChatGroupDTO() {}

    public ChatGroupDTO(Integer id, String name, ProfileDTO admin, Set<ProfileDTO> members, List<ChatMessageDTO> messages) {
        this.id = id;
        this.name = name;
        this.admin = admin;
        this.members = members;
        this.messages = messages;
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

    public ProfileDTO getAdmin() {
        return admin;
    }

    public void setAdmin(ProfileDTO admin) {
        this.admin = admin;
    }

    public Set<ProfileDTO> getMembers() {
        return members;
    }

    public void setMembers(Set<ProfileDTO> members) {
        this.members = members;
    }

    public List<ChatMessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessageDTO> messages) {
        this.messages = messages;
    }
}
