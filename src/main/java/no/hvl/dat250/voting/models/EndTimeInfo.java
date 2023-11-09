package no.hvl.dat250.voting.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EndTimeInfo {
    public Long pollId;
    public LocalDateTime endTime;

    // Deserialization method from JSON string
    public static EndTimeInfo fromJsonString(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, EndTimeInfo.class);
    }

    // Helper method to deserialize the time in a custom format if necessary
    @JsonCreator
    public EndTimeInfo(@JsonProperty("pollId") Long pollId, @JsonProperty("endTime") String endTime) {
        this.pollId = pollId;
        this.endTime = LocalDateTime.parse(endTime);
    }
}