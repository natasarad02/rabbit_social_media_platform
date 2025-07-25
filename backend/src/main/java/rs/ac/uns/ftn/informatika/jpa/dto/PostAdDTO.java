package rs.ac.uns.ftn.informatika.jpa.dto;


import java.io.Serializable;
import java.time.LocalDateTime;

public class PostAdDTO  implements Serializable {
    private String description;
    private String publishedTime;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PostAdDTO() {}
    public PostAdDTO(String description, String publishedTime, String username) {
        this.description = description;
        this.publishedTime = publishedTime;
        this.username = username;
    }

    public String getPublishedTime() {
        return publishedTime;
    }

    public void setPublishedTime(String publishedTime) {
        this.publishedTime = publishedTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;
}
