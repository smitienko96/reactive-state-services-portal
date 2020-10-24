package test.common.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.kafka.support.serializer.JsonSerializer;

/**
 * @author s.smitienko
 */
public class EventHolderSerde extends JsonSerde<AggregateEventsHolder> {

    public EventHolderSerde() {
        this(ObjectMapperFactory.createKafkaStateStoreObjectMapper());
    }


    public EventHolderSerde(Class<AggregateEventsHolder> targetType) {
        super(targetType);
    }

    public EventHolderSerde(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    public EventHolderSerde(Class<AggregateEventsHolder> targetType, ObjectMapper objectMapper) {
        super(targetType, objectMapper);
    }

    public EventHolderSerde(JsonSerializer<AggregateEventsHolder> jsonSerializer, JsonDeserializer<AggregateEventsHolder> jsonDeserializer) {
        super(jsonSerializer, jsonDeserializer);
    }

}
