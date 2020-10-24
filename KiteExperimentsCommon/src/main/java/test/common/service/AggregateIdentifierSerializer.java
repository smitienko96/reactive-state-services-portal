package test.common.service;

import org.apache.kafka.common.serialization.Serializer;
import test.common.domain.AggregateIdentifier;

import java.util.Map;

/**
 * @author s.smitienko
 */
public class AggregateIdentifierSerializer implements Serializer<AggregateIdentifier> {

    private AggregateIdentifierSerde serde = new AggregateIdentifierSerde();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        serde.serializer().configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String topic, AggregateIdentifier data) {
        return serde.serializer().serialize(topic, data);
    }

    @Override
    public void close() {
        serde.serializer().close();
    }
}
