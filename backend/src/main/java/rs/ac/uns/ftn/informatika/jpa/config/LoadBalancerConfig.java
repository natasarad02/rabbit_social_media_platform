package rs.ac.uns.ftn.informatika.jpa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class LoadBalancerConfig {

    @Value("#{'${loadbalancer.instances}'.split(',')}")
    private List<String> instances;

    @Value("${loadbalancer.strategy:round-robin}")
    private String strategy;

    @Value("${loadbalancer.retry.attempts:3}")
    private int retryAttempts;

    public List<String> getInstances() {
        return instances;
    }

    public String getStrategy() {
        return strategy;
    }

    public int getRetryAttempts() {
        return retryAttempts;
    }
}