package rs.ac.uns.ftn.informatika.jpa.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("rabbitmq")
public class RabbitMQConfig {

    public static String EXCHANGE_NAME = "advertising_exchange";

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue queue1() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue queue2() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding binding1(Queue queue1, FanoutExchange exchange) {
        return BindingBuilder.bind(queue1).to(exchange);
    }

    @Bean
    public Binding binding2(Queue queue2, FanoutExchange exchange) {
        return BindingBuilder.bind(queue2).to(exchange);
    }

    // ------------------------------------------------------------------------
    // ------------------------------- LOKACIJE -------------------------------
    // ------------------------------------------------------------------------
    @Value("${rabbit.location.exchange.name}")
    private String locationExchangeName;

    @Value("${rabbit.location.queue.name}")
    private String locationQueueName;

    @Value("${rabbit.location.routing.key}")
    private String locationRoutingKey;


    @Bean
    public DirectExchange locationExchange() {
        return new DirectExchange(locationExchangeName);
    }

    @Bean
    public Queue locationQueue() {
        return new Queue(locationQueueName, true);
    }

    @Bean
    public Binding locationBinding(Queue locationQueue, DirectExchange locationExchange) {
        return BindingBuilder.bind(locationQueue).to(locationExchange).with(locationRoutingKey);
    }
}
