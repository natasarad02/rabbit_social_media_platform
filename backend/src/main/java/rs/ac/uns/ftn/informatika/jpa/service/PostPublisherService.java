package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.dto.PostAdDTO;

@Service
public class PostPublisherService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public PostPublisherService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendPostForAds(String postAd)
    {
        rabbitTemplate.convertAndSend("advertising_exchange", "", postAd);
    }
}
