package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.service.inferface.PostPublisher;

@Service
@Profile("rabbitmq")
public class RabbitMqPostPublisher implements PostPublisher {
    private final RabbitTemplate rabbitTemplate;
    public RabbitMqPostPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        System.out.println("Using: RabbitMQ");
    }
    @Override
    public void publish(String postAd) {
        System.out.println("RabbitMQ");
        rabbitTemplate.convertAndSend("advertising_exchange", "", postAd);
    }
}