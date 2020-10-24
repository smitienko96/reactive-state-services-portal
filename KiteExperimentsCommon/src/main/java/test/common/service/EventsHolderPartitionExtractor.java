package test.common.service;

import org.springframework.cloud.stream.binder.PartitionKeyExtractorStrategy;
import org.springframework.messaging.Message;

public class EventsHolderPartitionExtractor implements PartitionKeyExtractorStrategy {

    @Override
    public Object extractKey(Message<?> message) {
        AggregateEventsHolder eventsHolder = (AggregateEventsHolder) message.getPayload();
        return eventsHolder.getInitialEvent().aggregateIdentifier().id();
    }
}
