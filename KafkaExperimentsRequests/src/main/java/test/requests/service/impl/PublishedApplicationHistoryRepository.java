package test.requests.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import test.common.domain.AggregateNames;
import test.common.domain.DomainEvent;
import test.common.domain.EventStream;
import test.common.service.IEventStore;
import test.requests.domain.ApplicationNumber;
import test.requests.domain.IPublishedApplicationHistoryRepository;
import test.requests.domain.RequestsEventType;
import test.requests.domain.published.HistorySearchFilter;
import test.requests.domain.published.PublishedApplicationHistory;
import test.requests.domain.published.PublishedApplicationHistoryEntry;
import test.requests.service.impl.historymapper.ApplicationHistoryFormatter;

import java.util.*;

@Component
@Slf4j
public class PublishedApplicationHistoryRepository implements IPublishedApplicationHistoryRepository {

    @Autowired
    private IEventStore eventStore;

    private Map<Class<DomainEvent>, ApplicationHistoryFormatter> historyFormatters;

    @Override
    public Mono<PublishedApplicationHistory> getApplicationHistory(String applicationNumber) {
        return eventStore.loadFullEventStream(AggregateNames.APPLICATIONS,
                new ApplicationNumber(applicationNumber))
                .map(this::convertEventStreamToHistory)
                .checkpoint();
    }


    private PublishedApplicationHistory convertEventStreamToHistory(EventStream stream) {
        PublishedApplicationHistory history = new PublishedApplicationHistory();
        while (stream.hasNext()) {
            DomainEvent<ApplicationNumber, RequestsEventType> event = stream.next();
            ApplicationHistoryFormatter formatter = historyFormatters.get(event.getClass());
            if (formatter == null) {
                log.warn("No history formatter found for event type [{}] to format to history entry", event.eventType().name());
                continue;
            }

            history = formatter.format(history, event);
        }
        return history;
    }

    @Override
    public Flux<PublishedApplicationHistoryEntry> searchEntries(HistorySearchFilter searchFilter) {
        return Flux.error(new RuntimeException("Not yet implemented"));
    }

    @Autowired
    public void setHistoryFormatters(Set<ApplicationHistoryFormatter> formatters) {
        historyFormatters = new HashMap<>(formatters.size());
        formatters.forEach(e -> historyFormatters.put(e.getEventClass(), e));
    }
}
