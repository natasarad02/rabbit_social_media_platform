package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.dto.PostAdDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.PostDTO;

@Service
@Profile("rabbitmq")
public class PostSubscriberService {

    @RabbitListener(queues = "#{queue1.name}")
    public void receivePost1(String post) {
        System.out.println("Agencija 1 primila post: " + post);
    }

    @RabbitListener(queues = "#{queue2.name}")
    public void receivePost2(String post) {
        System.out.println("Agencija 2 primila post: " + post);
    }
}
