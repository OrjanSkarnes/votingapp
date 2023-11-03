package no.hvl.dat250.voting.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import no.hvl.dat250.voting.models.Poll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {


    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendPoll(String topic, Poll poll) {
        try {
            String pollJson = mapper.writeValueAsString(poll);
            kafkaTemplate.send(topic, pollJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendObject(String topic, Object obj) {
        try {
            String pollJson = mapper.writeValueAsString(obj);
            kafkaTemplate.send(topic, pollJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
