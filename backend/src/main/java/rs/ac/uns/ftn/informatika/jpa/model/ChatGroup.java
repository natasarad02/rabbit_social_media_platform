package rs.ac.uns.ftn.informatika.jpa.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SQLDelete(sql
        = "UPDATE chat_group "
        + "SET deleted = true "
        + "WHERE id = ?")
@Where(clause = "deleted = false")
@Entity
public class ChatGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Profile admin;

    @OneToMany(mappedBy = "chatGroup")
    private Set<ChatGroupMember> members;

    @Column(name = "deleted")
    private boolean deleted;


    @OneToMany(mappedBy = "chatGroup", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ChatMessage> messages = new ArrayList<>();
    public ChatGroup() {}

    public ChatGroup(Integer id, String name, Profile admin, Set<ChatGroupMember> members) {
        this.id = id;
        this.name = name;
        this.admin = admin;
        this.members = members;

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

    public Profile getAdmin() {
        return admin;
    }

    public void setAdmin(Profile admin) {
        this.admin = admin;
    }

    public Set<ChatGroupMember> getMembers() {
        return members;
    }

    public void setMembers(Set<ChatGroupMember> members) {
        this.members = members;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
