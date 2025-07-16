package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.service.inferface.PostPublisher;

@Service
public class PostPublisherService {
    private final PostPublisher publisher;

    public PostPublisherService(PostPublisher publisher) {
        this.publisher = publisher;
    }

    public void sendPostForAds(String postAd) {
        publisher.publish(postAd);
    }
}