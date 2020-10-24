package test.requests.service.impl.historymapper;

import org.springframework.stereotype.Component;
import test.requests.domain.Status;
import test.requests.domain.events.ApplicationArchivedEvent;
import test.requests.domain.published.PublishedApplicationHistory;
import test.requests.domain.published.PublishedApplicationHistoryEntry;

import java.text.MessageFormat;

@Component
public class ApplicationArchivedHistoryFormatter extends ApplicationHistoryFormatter<ApplicationArchivedEvent> {

    @Override
    public PublishedApplicationHistory format(PublishedApplicationHistory original, ApplicationArchivedEvent event) {
        PublishedApplicationHistory history =  super.format(original, event);
        history.setApplicationStatus(Status.ARCHIVE.readable());
        return history;
    }

    @Override
    protected PublishedApplicationHistoryEntry format(PublishedApplicationHistoryEntry entry, ApplicationArchivedEvent event) {
        entry.setChangeDescription(MessageFormat.format("Заявление [{0}] было сдано в архив и не доступно для редактирования",
                event.aggregateIdentifier().number()));
        return entry;
    }

    @Override
    public Class<ApplicationArchivedEvent> getEventClass() {
        return ApplicationArchivedEvent.class;
    }
}
