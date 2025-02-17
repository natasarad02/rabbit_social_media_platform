package rs.ac.uns.ftn.informatika.jpa.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SQLDelete(sql
        = "UPDATE post "
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

    @OneToMany(mappedBy = "chatGroup", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Profile> members = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "chat_group_messages", joinColumns = @JoinColumn(name = "chat_group_id"))
    @Column(name = "message")
    private List<String> messages = new ArrayList<>();

    public ChatGroup() {}

    public ChatGroup(Integer id, String name, Profile admin, Set<Profile> members, List<String> messages) {
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

    public Profile getAdmin() {
        return admin;
    }

    public void setAdmin(Profile admin) {
        this.admin = admin;
    }

    public Set<Profile> getMembers() {
        return members;
    }

    public void setMembers(Set<Profile> members) {
        this.members = members;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
