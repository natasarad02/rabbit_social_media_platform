package rs.ac.uns.ftn.informatika.jpa.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// Import your DTO
import rs.ac.uns.ftn.informatika.jpa.dto.RabbitLocationMessageDTO; // Make sure this import is correct
import rs.ac.uns.ftn.informatika.jpa.model.RabbitLocation; // Import your Entity
import rs.ac.uns.ftn.informatika.jpa.repository.RabbitLocationRepository; // Import your Repository

@Service
public class RabbitLocationService {

    private final RabbitLocationRepository rabbitLocationRepository;

    @Autowired
    public RabbitLocationService(RabbitLocationRepository rabbitLocationRepository) {
        this.rabbitLocationRepository = rabbitLocationRepository;
    }

    @Transactional
    public RabbitLocation saveLocation(RabbitLocationMessageDTO locationDto) {
        RabbitLocation locationEntity = new RabbitLocation( 
                locationDto.getId(), // Get ID from DTO
                locationDto.getName(), // Get name from DTO (using new getName method)
                locationDto.getLocationData().getLatitude(), // Get latitude from nested GeoLocation in DTO
                locationDto.getLocationData().getLongitude() // Get longitude from nested GeoLocation in DTO
        );

        System.out.println("-> SERVICE: Attempting to save/update location with ID: " + locationEntity.getId());
        RabbitLocation savedLocationEntity = rabbitLocationRepository.save(locationEntity); // Variable name changed
        System.out.println("-> SERVICE: Location saved/updated successfully: " + savedLocationEntity.getId());

        return savedLocationEntity;
    }

    // Method to get all locations for display on the map
    public java.util.List<RabbitLocation> getAllLocations() {
        System.out.println("-> SERVICE: Fetching all rabbit locations.");
        return rabbitLocationRepository.findAll();
    }

    // Optional: Method to get a single location by ID if needed
    public RabbitLocation getLocationById(String id) {
        System.out.println("-> SERVICE: Fetching rabbit location with ID: " + id);
        return rabbitLocationRepository.findById(id).orElse(null);
    }
}
