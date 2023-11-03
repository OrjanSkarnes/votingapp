package no.hvl.dat250.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

// This is a service that consumes messages from the Kafka topic "pollResults" and could be in a another microservice
@Service
public class KafkaConsumerService {

    @Autowired
    private LoggerService loggerService;

    @KafkaListener(topics = "pollResults")
    public void consume(String pollResults) {
        String dweetUrl = "https://dweet.io/dweet/for/your-unique-thing-name";
        
        // Post the poll results to dweet.io
        try {
            //restTemplate.postForObject(dweetUrl, pollResults, String.class);
            loggerService.log("Poll results posted to dweet.io successfully.");
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

}
