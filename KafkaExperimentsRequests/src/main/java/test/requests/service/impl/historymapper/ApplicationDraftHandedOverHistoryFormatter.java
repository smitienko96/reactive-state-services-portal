package test.requests.service.impl.historymapper;

import org.springframework.stereotype.Component;
import test.requests.domain.Status;
import test.requests.domain.events.ApplicationDraftHandedOverEvent;
import test.requests.domain.published.PublishedApplicationHistory;
import test.requests.domain.published.PublishedApplicationHistoryEntry;

import java.text.MessageFormat;

@Component
public class ApplicationDraftHandedOverHistoryFormatter extends ApplicationHistoryFormatter<ApplicationDraftHandedOverEvent> {

    @Override
    public PublishedApplicationHistory format(PublishedApplicationHistory original, ApplicationDraftHandedOverEvent event) {
        PublishedApplicationHistory history = super.format(original, event);

        history.setApplicantId(event.applicantId().id());
        history.setAppicationNumber(event.aggregateIdentifier().number());
        history.setApplicationStatus(Status.DRAFT.readable());

        return history;
    }

    @Override
    public PublishedApplicationHistoryEntry format(PublishedApplicationHistoryEntry entry, ApplicationDraftHandedOverEvent event) {
        entry.setChangeDescription(MessageFormat.format("Заявителю выдан бланк заявления типа [{0}] с {1} заполненными полями",
                event.applicationType().typeCode(), event.documentFields().size()));
        entry.setOperatorId(event.operatorId().id());
        return entry;
    }

    @Override
    public Class<ApplicationDraftHandedOverEvent> getEventClass() {
        return ApplicationDraftHandedOverEvent.class;
    }
}
