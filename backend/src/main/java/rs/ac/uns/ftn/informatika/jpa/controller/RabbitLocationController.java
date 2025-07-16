package rs.ac.uns.ftn.informatika.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.informatika.jpa.model.RabbitLocation; // Import vaseg entiteta
import rs.ac.uns.ftn.informatika.jpa.service.RabbitLocationService; // Import vaseg Service-a

import java.util.List;

@RestController
@RequestMapping("/api/rabbit-locations")
public class RabbitLocationController {

    private final RabbitLocationService rabbitLocationService;

    @Autowired
    public RabbitLocationController(RabbitLocationService rabbitLocationService) {
        this.rabbitLocationService = rabbitLocationService;
    }

    @GetMapping
    public ResponseEntity<List<RabbitLocation>> getAllRabbitLocations() {
        List<RabbitLocation> locations = rabbitLocationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }


}
