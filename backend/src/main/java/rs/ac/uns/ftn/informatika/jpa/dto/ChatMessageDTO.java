package rs.ac.uns.ftn.informatika.jpa.dto;

import java.time.LocalDateTime;

public class ChatMessageDTO {
    private Integer id;
    private String message;
    private Integer senderId;
    private Integer receiverId;
    private Integer chatGroupId;
    private LocalDateTime timestamp;

    public ChatMessageDTO() {}

    public ChatMessageDTO(Integer id, String message, Integer sender, Integer receiver, Integer chatGroup, LocalDateTime timestamp) {
        this.id = id;
        this.message = message;
        this.senderId = sender;
        this.receiverId = receiver;
        this.chatGroupId = chatGroup;
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

    public Integer getSender() {
        return senderId;
    }

    public void setSender(Integer sender) {
        this.senderId = sender;
    }

    public Integer getReceiver() {
        return receiverId;
    }

    public void setReceiver(Integer receiver) {
        this.receiverId = receiver;
    }

    public Integer getChatGroup() {
        return chatGroupId;
    }

    public void setChatGroup(Integer chatGroup) {
        this.chatGroupId = chatGroup;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
