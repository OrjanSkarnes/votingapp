package no.hvl.dat250.voting.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

@Setter
@Getter
@Document
public class AnalyticsEvent {
    @Id
    private Long id;
    private String eventName;
    private Object eventData;
    private Instant timestamp;

    public AnalyticsEvent(String eventName, Object eventData) {
        this.eventName = eventName;
        this.eventData = eventData;
    }
}
