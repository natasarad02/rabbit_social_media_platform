package rs.ac.uns.ftn.informatika.jpa.listener;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
// Import your DTO
import rs.ac.uns.ftn.informatika.jpa.dto.RabbitLocationMessageDTO; // Make sure this import is correct
// Import your Service for saving data
import rs.ac.uns.ftn.informatika.jpa.service.RabbitLocationService; // EXAMPLE: Adjust package name

@Component
public class RabbitLocationMessageListener {

    private final ObjectMapper objectMapper = new ObjectMapper(); // For JSON parsing
    private final RabbitLocationService rabbitLocationService; // Service for saving will be injected

    // Inject Service via constructor
    @Autowired
    public RabbitLocationMessageListener(RabbitLocationService rabbitLocationService) {
        this.rabbitLocationService = rabbitLocationService;
    }


    // Listens on the queue whose name is defined by the rabbit.location.queue.name property
    @RabbitListener(queues = "${rabbit.location.queue.name}")
    public void handleLocationUpdate(String messagePayload) { // Renamed messagePayload variable
        System.out.println("-> LOCATIONS MQ LISTENER: Received location message: " + messagePayload);
        try {
            // Parse the JSON message into your DTO object
            // Use the updated DTO class name
            RabbitLocationMessageDTO locationData = objectMapper.readValue(messagePayload, RabbitLocationMessageDTO.class); // Renamed locationData variable

            // CALL YOUR SERVICE to save location data in the database
            // Implement the saveLocation method in the Service class (Next step)
            rabbitLocationService.saveLocation(locationData); // Pass the parsed DTO

            System.out.println("-> LOCATIONS MQ LISTENER: Successfully processed location: ID=" + locationData.getId() + ", Name=" + locationData.getName()); // Use getName() from DTO

        } catch (Exception e) {
            System.err.println("-> LOCATIONS MQ LISTENER: Error processing message: " + e.getMessage());
            // Optional: Implement error handling logic (e.g., logging, sending to Dead Letter Queue)
        }
    }
}
