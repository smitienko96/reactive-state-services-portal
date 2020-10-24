package test.requests.service.impl.historymapper;

import org.springframework.stereotype.Component;
import test.requests.domain.events.ApplicationStatusChangedEvent;
import test.requests.domain.published.PublishedApplicationHistory;
import test.requests.domain.published.PublishedApplicationHistoryEntry;

import java.text.MessageFormat;

@Component
public class ApplicationStatusChangedHistoryFormatter extends ApplicationHistoryFormatter<ApplicationStatusChangedEvent> {

    @Override
    public PublishedApplicationHistory format(PublishedApplicationHistory original, ApplicationStatusChangedEvent event) {
        PublishedApplicationHistory history = super.format(original, event);
        history.setApplicationStatus(event.newStatus().name());
        return history;
    }

    @Override
    protected PublishedApplicationHistoryEntry format(PublishedApplicationHistoryEntry entry, ApplicationStatusChangedEvent event) {
        entry.setChangeDescription(MessageFormat.format("Заявление было переведено в состояние [{0}]", event.newStatus().readable()));
        return entry;
    }

    @Override
    public Class<ApplicationStatusChangedEvent> getEventClass() {
        return ApplicationStatusChangedEvent.class;
    }
}
