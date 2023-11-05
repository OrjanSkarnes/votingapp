package no.hvl.dat250.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.hvl.dat250.voting.models.Poll;
@Service
public class KafkaConsumerService {
    @Autowired
    private LoggerService loggerService;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "pollResults")
    public void consume(String pollResults) {
        String dweetUrl = "";
        String jsonPayload = "";
        String response = "";
        Poll poll = null;

        try {
            poll = objectMapper.readValue(pollResults, Poll.class);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            jsonPayload = objectMapper.writeValueAsString(poll);
            HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

            dweetUrl = "https://dweet.io/dweet/for/votingapp-poll-results-" + poll.getId();

            response = restTemplate.postForObject(dweetUrl, request, String.class);
        } catch (Exception e) {
            loggerService.logError("Error during dweet.io POST operation: Poll ID " + (poll != null ? poll.getId() : "unknown")
                    + ", Payload: " + jsonPayload
                    + ", Dweet URL: " + dweetUrl
                    + ", Exception: " + e.getMessage(), e);
            return;
        }

        loggerService.log(String.format("Successfully posted poll results to dweet.io: Poll ID: %d, Dweet URL: %s, Response: %s", poll.getId(), "https://dweet.io/get/latest/dweet/for/votingapp-poll-results-"+poll.getId(), response));
    }
}
