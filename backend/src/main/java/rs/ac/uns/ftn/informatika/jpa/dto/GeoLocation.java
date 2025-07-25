package rs.ac.uns.ftn.informatika.jpa.dto;

public class GeoLocation {

    private double latitude;
    private double longitude;

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public GeoLocation() {}

    public GeoLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}