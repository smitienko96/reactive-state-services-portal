package test.common.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import test.common.service.AggregateEventsHolder;
import test.common.service.EventStoreConstants;
import test.common.service.EventsHolderQueryServiceKafka;

@RestController
@RequestMapping(path = EventStoreConstants.KAFKA_REMOTE_STATE_STORE_PATH,
        produces=EventStoreConstants.KAFKA_EVENTS_STREAM_MIME_TYPE)
public class KafkaEventStreamController {

    @Autowired
    private EventsHolderQueryServiceKafka queryServiceKafka;

    @GetMapping(path = "/{storeName}/{aggregateId}")
    public Mono<AggregateEventsHolder> getAggregateEventsStream(@PathVariable String storeName, @PathVariable String aggregateId) {
        return queryServiceKafka.getEventsHolderForAggregate(aggregateId, storeName);
    }
}
