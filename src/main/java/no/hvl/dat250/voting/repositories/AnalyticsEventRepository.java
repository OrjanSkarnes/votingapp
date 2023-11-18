package no.hvl.dat250.voting.repositories;

import no.hvl.dat250.voting.models.AnalyticsEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnalyticsEventRepository extends MongoRepository<AnalyticsEvent, String> {
}
