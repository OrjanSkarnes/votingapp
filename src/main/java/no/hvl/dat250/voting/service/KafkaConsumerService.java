package no.hvl.dat250.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {


    @KafkaListener(topics = "pollResults")
    public void consume(String pollResults) {
        System.out.println("Consumed message: " + pollResults);
        String dweetUrl = "https://dweet.io/dweet/for/your-unique-thing-name";
        
        // Post the poll results to dweet.io
        try {
            //restTemplate.postForObject(dweetUrl, pollResults, String.class);
            System.out.println("Poll results posted to dweet.io successfully.");
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
}
