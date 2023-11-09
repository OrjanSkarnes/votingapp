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

    @Autowired
    private LoggerService loggerService;

    public void sendObject(String topic, Object obj) {
        try {
            String pollJson = mapper.writeValueAsString(obj);
            kafkaTemplate.send(topic, pollJson);
            loggerService.log("Object sent to kafka topic: " + topic);
        } catch (Exception e) {
            loggerService.logError("Error sending object to kafka topic: " + topic, e);
        }
    }
}
