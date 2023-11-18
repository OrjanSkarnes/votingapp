package no.hvl.dat250.voting.controller;

import no.hvl.dat250.voting.models.AnalyticsEvent;
import no.hvl.dat250.voting.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @Autowired
    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @PostMapping
    public ResponseEntity<?> trackEvent(@RequestBody AnalyticsEvent event) {
        analyticsService.saveEvent(event);
        return ResponseEntity.ok().build();
    }
}

