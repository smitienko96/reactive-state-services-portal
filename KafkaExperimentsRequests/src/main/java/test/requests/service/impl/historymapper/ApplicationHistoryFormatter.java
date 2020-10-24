package test.requests.service.impl.historymapper;

import test.common.domain.DomainEvent;
import test.requests.domain.ApplicationNumber;
import test.requests.domain.RequestsEventType;
import test.requests.domain.published.PublishedApplicationHistory;
import test.requests.domain.published.PublishedApplicationHistoryEntry;

public abstract class ApplicationHistoryFormatter<E extends DomainEvent<ApplicationNumber, RequestsEventType>> {

    public PublishedApplicationHistory format(PublishedApplicationHistory original,
                                              E event) {
        original.setLastChangedDate(event.date());
        original.setAppicationNumber(event.aggregateIdentifier().number());


        PublishedApplicationHistoryEntry entry = new PublishedApplicationHistoryEntry();
        entry.setChangeDate(event.date());
        entry.setChangeType(event.eventType().description());

        entry = format(entry, event);
        original.addEntry(entry);

        return original;
    }

    protected abstract PublishedApplicationHistoryEntry format(PublishedApplicationHistoryEntry entry, E event);


    public abstract Class<E> getEventClass();

}
