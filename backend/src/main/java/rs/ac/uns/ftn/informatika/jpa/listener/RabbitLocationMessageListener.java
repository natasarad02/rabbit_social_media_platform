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

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RabbitLocationService rabbitLocationService;

    @Autowired
    public RabbitLocationMessageListener(RabbitLocationService rabbitLocationService) {
        this.rabbitLocationService = rabbitLocationService;
    }


    @RabbitListener(queues = "${rabbit.location.queue.name}")
    public void handleLocationUpdate(String messagePayload) {
        try {
            RabbitLocationMessageDTO locationData = objectMapper.readValue(messagePayload, RabbitLocationMessageDTO.class); // Renamed locationData variable

            rabbitLocationService.saveLocation(locationData);

            System.out.println("Successfully processed location: ID=" + locationData.getId() + ", Name=" + locationData.getName()); // Use getName() from DTO

        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }
}
