package rs.ac.uns.ftn.informatika.jpa.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@SQLDelete(sql
        = "UPDATE chat_message "
        + "SET deleted = true "
        + "WHERE id = ?")
@Where(clause = "deleted = false")
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "message", nullable = false)
    private String message;

    @OneToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private Profile sender;

    @OneToOne
    @JoinColumn(name = "receiver_id", nullable = true)
    private Profile receiver;

    @ManyToOne(fetch = FetchType.EAGER )
    @JoinColumn(name = "chat_group_id" , nullable = true)
    private ChatGroup chatGroup;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(name = "deleted")
    private boolean deleted;

    public ChatMessage() {}

    public ChatMessage(Integer id, String message, Profile sender, Profile receiver, ChatGroup chatGroup, LocalDateTime timestamp) {
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

    public Profile getSender() {
        return sender;
    }

    public void setSender(Profile sender) {
        this.sender = sender;
    }

    public Profile getReceiver() {
        return receiver;
    }

    public void setReceiver(Profile receiver) {
        this.receiver = receiver;
    }

    public ChatGroup getChatGroup() {
        return chatGroup;
    }

    public void setChatGroup(ChatGroup chatGroup) {
        this.chatGroup = chatGroup;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
