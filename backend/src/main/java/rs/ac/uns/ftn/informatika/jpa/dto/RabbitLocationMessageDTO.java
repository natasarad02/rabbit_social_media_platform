package rs.ac.uns.ftn.informatika.jpa.dto;

public class RabbitLocationMessageDTO {

    private String id;
    private String name; // Renamed from naziv
    private GeoLocation locationData; // Renamed from lokacija and type changed to GeoLocation

    // Manual Getters, Setters, Constructor if not using Lombok:
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; } // Getter for name
    public void setName(String name) { this.name = name; } // Setter for name
    public GeoLocation getLocationData() { return locationData; } // Getter for locationData
    public void setLocationData(GeoLocation locationData) { this.locationData = locationData; } // Setter for locationData


    public RabbitLocationMessageDTO() {}

    public RabbitLocationMessageDTO(String id, String name, GeoLocation locationData) {
        this.id = id;
        this.name = name;
        this.locationData = locationData;
    }
}
