package rs.ac.uns.ftn.informatika.jpa.dto;

public class RabbitLocationMessageDTO {

    private String id;
    private String name;
    private GeoLocation locationData;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public GeoLocation getLocationData() { return locationData; }
    public void setLocationData(GeoLocation locationData) { this.locationData = locationData; }

    public RabbitLocationMessageDTO() {}

    public RabbitLocationMessageDTO(String id, String name, GeoLocation locationData) {
        this.id = id;
        this.name = name;
        this.locationData = locationData;
    }
}
