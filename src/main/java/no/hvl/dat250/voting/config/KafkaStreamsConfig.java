package no.hvl.dat250.voting.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.hvl.dat250.voting.models.EndTimeInfo;
import no.hvl.dat250.voting.service.LoggerService;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Duration;
import java.time.ZoneId;
import java.util.Properties;
// This code is part of a configuration class for a Kafka Streams application in a Spring Boot project.
@Configuration
public class KafkaStreamsConfig {

    // These values are taken from the application's properties file.
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.poll-stream-topic}")
    private String pollEndTimeTopic;

    @Value("${spring.kafka.streams.application-id}")
    private String applicationId;

    @Bean
    public KafkaStreams kafkaStreams() {
        final String finishPollTopic = "pollEndTimesFinished";

        // Properties for the Kafka Streams application.
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 4);
        props.put(StreamsConfig.NUM_STANDBY_REPLICAS_CONFIG, 1);


        // StreamsBuilder is used to build the topology (flow) of the Kafka Streams application.
        StreamsBuilder builder = new StreamsBuilder();

        // A state store is used to keep track of the end times for polls.
        builder.addStateStore(Stores.keyValueStoreBuilder(
                Stores.inMemoryKeyValueStore("poll-end-times"),
                Serdes.String(),
                Serdes.Long()));

        // Define a stream that reads from the poll end times topic.
        KStream<String, String> endTimes = builder.stream(
                pollEndTimeTopic,
                Consumed.with(Serdes.String(), Serdes.String())
        );

        // Process the stream using a custom processor and then publish the results to another topic.
        endTimes.process(PollEndTimeProcessor::new, "poll-end-times")
                // This is the last step in the topology, so the results are published to a topic.
                .to(finishPollTopic, Produced.with(Serdes.String(), Serdes.String()));

        // Building the final topology that defines the flow of data through the Kafka Streams application.
        final Topology topology = builder.build();

        // Creating the Kafka Streams instance with the configuration and topology.
        KafkaStreams streams = new KafkaStreams(topology, props);

        streams.start();

        // Ensures that the Kafka Streams application is cleanly shut down when the JVM exits.
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        return streams;
    }

    // A custom processor for handling records in the stream.
    private static class PollEndTimeProcessor implements Processor<String, String, String, String> {
        private ProcessorContext context;
        private KeyValueStore<String, Long> store;

        @Override
        public void init(ProcessorContext context) {
            this.store = context.getStateStore("poll-end-times");
            this.context = context;
            // This sets up a recurring task that checks the poll end times every second.
            // Can be changed to a different interval or to a different type of punctuation.
            this.context.schedule(Duration.ofSeconds(1), PunctuationType.WALL_CLOCK_TIME, this::punctuate);
        }

        @Override
        public void process(Record<String, String> record) {
            System.out.println("Received record: " + record.value());

            try {
                // Deserialize JSON string to EndTimeInfo object and store it with the poll ID as the key.
                EndTimeInfo endTimeInfo = EndTimeInfo.fromJsonString(record.value());
                store.put(String.valueOf(endTimeInfo.pollId), endTimeInfo.getEndTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            } catch (IOException e) {
                throw new RuntimeException("Failed to deserialize JSON", e);
            }
        }

        // This method is ran on schedule to check if any polls have ended. If so, the poll is deleted from the state store.
        private void punctuate(long timestamp) {
            // Iterating over all entries in the state store.
            try (KeyValueIterator<String, Long> it = store.all()) {
                while (it.hasNext()) {
                    KeyValue<String, Long> entry = it.next();
                    // If the poll end time is before or at the current time, the poll has ended.
                    if (entry.value <= timestamp) {
                        System.out.println("Poll has ended: " + entry.key);
                        // Forward the poll ID to the next processor in the topology.
                        this.context.forward(new Record<>(entry.key, entry.key ,timestamp));
                        store.delete(entry.key);
                    }
                }
            }
        }
    }
}
