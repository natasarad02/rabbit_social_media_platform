package rs.ac.uns.ftn.informatika.jpa.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
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

    // Ucitavanje naziva komponenti iz application.properties za LOKACIJE
    @Value("${rabbit.location.exchange.name}") // Ucitava naziv vaseg Direct Exchange-a
    private String locationExchangeName;

    @Value("${rabbit.location.queue.name}") // Ucitava naziv vaseg Reda
    private String locationQueueName;

    @Value("${rabbit.location.routing.key}") // Ucitava naziv vaseg Routing Key-a
    private String locationRoutingKey;


    // Definise NOVI Direct Exchange za lokacije zecje brige
    // Ovaj Bean se razlikuje od fanoutExchange() po tipu i nazivu
    @Bean
    public DirectExchange locationExchange() {
        return new DirectExchange(locationExchangeName);
    }

    // Definise NOVI, IMENOVANI Red za lokacije zecje brige
    // Ovo NIJE AnonymousQueue. Red je durable=true da bi preživeo restart brokera.
    @Bean
    public Queue locationQueue() {
        // Postojanost (durable=true) je bitna da se poruke ne izgube pri restartu brokera/aplikacije
        return new Queue(locationQueueName, true); // Postojan red za lokacije
    }

    // Definise Binding izmedju VASEG Reda i VASEG Direct Exchange-a koristeci VAS Routing Key
    // Imena argumenata (locationQueue, locationExchange) se poklapaju sa Bean metodama iznad
    @Bean
    public Binding locationBinding(Queue locationQueue, DirectExchange locationExchange) {
        return BindingBuilder.bind(locationQueue).to(locationExchange).with(locationRoutingKey);
    }
}
