package rs.ac.uns.ftn.informatika.jpa.dto;

import rs.ac.uns.ftn.informatika.jpa.model.ChatGroup;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;

import javax.persistence.*;
import java.time.LocalDateTime;

public class ChatMessageDTO {
    private Integer id;
    private String message;
    private ProfileDTO sender;
    private ProfileDTO receiver;
    private ChatGroupDTO chatGroup;
    private LocalDateTime timestamp;

    public ChatMessageDTO() {}

    public ChatMessageDTO(Integer id, String message, ProfileDTO sender, ProfileDTO receiver, ChatGroupDTO chatGroup, LocalDateTime timestamp) {
        this.id = id;
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.chatGroup = chatGroup;
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ProfileDTO getSender() {
        return sender;
    }

    public void setSender(ProfileDTO sender) {
        this.sender = sender;
    }

    public ProfileDTO getReceiver() {
        return receiver;
    }

    public void setReceiver(ProfileDTO receiver) {
        this.receiver = receiver;
    }

    public ChatGroupDTO getChatGroup() {
        return chatGroup;
    }

    public void setChatGroup(ChatGroupDTO chatGroup) {
        this.chatGroup = chatGroup;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
