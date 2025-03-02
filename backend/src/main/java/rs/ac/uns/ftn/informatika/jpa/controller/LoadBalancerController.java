package rs.ac.uns.ftn.informatika.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.informatika.jpa.config.LoadBalancerConfig;
import rs.ac.uns.ftn.informatika.jpa.service.LoadBalancerService;

@Controller
@RequestMapping("/api/balancer")
public class LoadBalancerController {
    private LoadBalancerService loadBalancerService;
    private RestTemplate restTemplate = new RestTemplate();
    private int maxRetries;

    public LoadBalancerController(@Autowired LoadBalancerService loadBalancerService, @Autowired LoadBalancerConfig config) {
        this.loadBalancerService = loadBalancerService;
        this.maxRetries = config.getRetryAttempts();
    }

    @GetMapping("/proxy")
    public ResponseEntity<String> proxyRequest(@RequestParam String data) {
        int attempts = 0;
        while (attempts < maxRetries) {
            try {
                String instanceUrl = loadBalancerService.getNextInstance() + "/api/handle?data=" + data;
                return restTemplate.getForEntity(instanceUrl, String.class);
            } catch (Exception e) {
                attempts++;
                if (attempts == maxRetries) {
                    return ResponseEntity.status(503).body("All instances are unavailable!");
                }
            }
        }
        return ResponseEntity.status(503).body("Failed to process request.");
    }
}
