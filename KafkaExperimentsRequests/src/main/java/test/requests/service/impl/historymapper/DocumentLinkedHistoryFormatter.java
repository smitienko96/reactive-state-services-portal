package test.requests.service.impl.historymapper;

import org.springframework.stereotype.Component;
import test.requests.domain.events.DocumentLinkedEvent;
import test.requests.domain.published.PublishedApplicationHistoryEntry;

import java.text.MessageFormat;

@Component
public class DocumentLinkedHistoryFormatter extends ApplicationHistoryFormatter<DocumentLinkedEvent> {

    @Override
    protected PublishedApplicationHistoryEntry format(PublishedApplicationHistoryEntry entry, DocumentLinkedEvent event) {
        entry.setChangeDescription(MessageFormat.format("К заявлению был привязан документ [{0}] \"{1}\" " +
                "с идентификатором [{2}]", event.documentType().typeCode(), event.name(), event.documentId().id()));
        return entry;
    }

    @Override
    public Class<DocumentLinkedEvent> getEventClass() {
        return DocumentLinkedEvent.class;
    }
}
