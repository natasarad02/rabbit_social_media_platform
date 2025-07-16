package rs.ac.uns.ftn.informatika.jpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import rs.ac.uns.ftn.informatika.jpa.mq.custom.QueueManager;

@Configuration
@Profile("custom")
public class CustomMqConfig {
    @Bean
    public QueueManager queueManager() {
        return QueueManager.getInstance();
    }
}