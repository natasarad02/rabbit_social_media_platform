package rs.ac.uns.ftn.informatika.jpa.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.jpa.dto.RabbitLocationMessageDTO;
import rs.ac.uns.ftn.informatika.jpa.mq.custom.*;
import rs.ac.uns.ftn.informatika.jpa.service.RabbitLocationService;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
@Profile("custom")
public class CustomMessageQueueRunner implements ApplicationRunner {
    private final QueueManager queueManager;
    private final RabbitLocationService locationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CustomMessageQueueRunner(QueueManager queueManager, RabbitLocationService locationService) {
        this.queueManager = queueManager;
        this.locationService = locationService;
    }

    @Override
    public void run(ApplicationArguments args) {
        queueManager.createQueue("bunny-care-location", QueueManager.QueueType.DIRECT);
        queueManager.createQueue("advertising_exchange", QueueManager.QueueType.FANOUT);

        startLocationConsumer();
        startAdAgencyConsumer("AdAgency1");
        startAdAgencyConsumer("AdAgency2");
    }

    private void startLocationConsumer() {
        new Thread(() -> {
            try {
                MessageQueue queue = queueManager.getQueue("bunny-care-location");
                while (true) {
                    Message message = queue.receive();
                    String payload = message.getContent();
                    RabbitLocationMessageDTO data = objectMapper.readValue(payload, RabbitLocationMessageDTO.class);
                    locationService.saveLocation(data);
                    System.out.println("Received " + data.getName());
                }
            } catch (Exception e) {
                Thread.currentThread().interrupt();
                System.err.println("Failure in custom location consumer");
            }
        }).start();
    }

    private void startAdAgencyConsumer(String agencyId) {
        new Thread(() -> {
            MessageQueue fanoutQueue = queueManager.getQueue("advertising_exchange");
            BlockingQueue<Message> personalQueue = new LinkedBlockingQueue<>();
            fanoutQueue.subscribe(agencyId, personalQueue);
            System.out.println(agencyId + " is subscribed to custom ads.");
            try {
                while (true) {
                    Message message = personalQueue.take();
                    System.out.println("-> [" + agencyId + "] primljena reklama: " + message.getContent());
                }
            } catch (InterruptedException e) {
                fanoutQueue.unsubscribe(agencyId);
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}