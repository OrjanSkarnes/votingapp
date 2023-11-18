package no.hvl.dat250.voting.service;

import no.hvl.dat250.voting.models.AnalyticsEvent;
import no.hvl.dat250.voting.repositories.AnalyticsEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class AnalyticsService {

    private final AnalyticsEventRepository analyticsEventRepository;

    @Autowired
    public AnalyticsService(AnalyticsEventRepository analyticsEventRepository) {
        this.analyticsEventRepository = analyticsEventRepository;
    }

    public void saveEvent(AnalyticsEvent event) {
        // Set the current time as the timestamp of the event
        event.setTimestamp(Instant.now());
        analyticsEventRepository.save(event);
    }

    // Additional methods as per your requirement...
}

