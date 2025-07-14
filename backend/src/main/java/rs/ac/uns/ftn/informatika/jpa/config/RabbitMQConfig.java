package rs.ac.uns.ftn.informatika.jpa.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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
}
