package rs.ac.uns.ftn.informatika.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.informatika.jpa.service.LoadBalancerService;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Arrays;

@RestController
@RequestMapping(value = "api/proxy")
public class LoadBalancerController {
    private LoadBalancerService loadBalancerService;
    @Value("${server.port}") // Učitava port iz application.properties ili default vrednosti
    private String port;

    public LoadBalancerController(@Autowired LoadBalancerService loadBalancerService) {
        this.loadBalancerService = loadBalancerService;
    }

    @GetMapping
    public ResponseEntity<String> proxyRequest() {
        return loadBalancerService.forwardRequest();
    }

    @GetMapping("/data")
    public String getData() {
        return "Odgovor sa instance na portu " + port;
    }
}
