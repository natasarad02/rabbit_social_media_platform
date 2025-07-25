package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.mq.custom.*;
import rs.ac.uns.ftn.informatika.jpa.service.inferface.PostPublisher;

@Service
@Profile("custom") // Only active when profile is "custom"
public class CustomPostPublisher implements PostPublisher {
    private final QueueManager queueManager;
    public CustomPostPublisher(QueueManager queueManager) {
        this.queueManager = queueManager;
        System.out.println("Using Custom MQ");
    }
    @Override
    public void publish(String postAd) {
        System.out.println("Custom MQ");
        try {
            queueManager.getQueue("advertising_exchange").send(new Message(postAd));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}