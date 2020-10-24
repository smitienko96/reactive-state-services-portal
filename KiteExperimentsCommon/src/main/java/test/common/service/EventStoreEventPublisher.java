package test.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import test.common.domain.AggregateIdentifier;
import test.common.domain.DomainEvent;
import test.common.domain.EventPublisher;

import java.util.List;

/**
 * @author s.smitienko
 */
@Slf4j
@Component
public class EventStoreEventPublisher implements EventPublisher {

    @Autowired
    private IEventStore eventStore;

    @Override
    public void publish(DomainEvent event) {
        Mono<Void> result = eventStore.appendToStream(event.aggregateName(),
                event.aggregateIdentifier(), event);

        result.subscribe(r -> {
            log.info("Event [{0}] successfully processed", event);
        }, exception -> {
            log.error("Exception occurred while processing event [{0}]",
                    new Object[]{event, exception});
        });
    }

    @Override
    public void publish(String aggregateName,
                        AggregateIdentifier identifier,
                        List<DomainEvent> events) {
        Mono<Void> result = eventStore.appendToStream(aggregateName,
                identifier, events);

        result.subscribe(r -> {
            log.info("{} events have been successfully processed",
                    events.size());
        }, exception -> {
            log.error("Exception occurred while processing events for " +
                            "aggregate [{}:{}]",
                    new Object[]{aggregateName, identifier,
                            exception});
        });
    }

    @Override
    public void publishError(Exception ex) {
        // TODO: implement
    }


}
