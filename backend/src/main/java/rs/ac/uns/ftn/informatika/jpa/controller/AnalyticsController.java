package rs.ac.uns.ftn.informatika.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.informatika.jpa.dto.AnalyticsDTO;
import rs.ac.uns.ftn.informatika.jpa.service.AnalyticsService;

@RestController
@RequestMapping(value = "api/analytics")
public class AnalyticsController {
    private AnalyticsService analyticsService;

    public AnalyticsController(@Autowired AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('Administrator')")
    public ResponseEntity<AnalyticsDTO> getAnalyticsData() {
        AnalyticsDTO analyticsData = analyticsService.getAnalyticsData();
        return ResponseEntity.ok(analyticsData);
    }


}
