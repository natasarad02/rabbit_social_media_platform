package rs.ac.uns.ftn.informatika.jpa.model;

import javax.persistence.*; // Use jakarta.persistence if you are on Spring Boot 3+

@Entity // Marks this as a JPA entity
@Table(name = "rabbit_locations") // Table name in the database
public class RabbitLocation {

    @Id
    private String id; // Assuming the ID from the message is a unique String

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    public RabbitLocation() {}

    public RabbitLocation(String id, String name, double latitude, double longitude) { // Updated constructor parameter names
        this.id = id;
        this.name = name; // Assign to 'name' field
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; } // Getter for name
    public void setName(String name) { this.name = name; } // Setter for name
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}