package test.regulations.domain.events;

import lombok.NoArgsConstructor;
import test.common.domain.AggregateNames;
import test.common.domain.AggregateVersion;
import test.common.domain.DocumentTypeId;
import test.common.domain.DomainEvent;
import test.regulations.domain.RegulationIdentifier;

@NoArgsConstructor
public class QuestionaryRegisteredEvent extends DomainEvent<RegulationIdentifier, RegulationsEventType> {

    private DocumentTypeId documentTypeId;

    public QuestionaryRegisteredEvent(long date, String uuid, RegulationIdentifier identifier,
                                      AggregateVersion version, DocumentTypeId documentTypeId) {
        super(date, uuid, identifier, version);
        this.documentTypeId = documentTypeId;
    }

    @Override
    public String aggregateName() {
        return AggregateNames.REGULATIONS;
    }

    @Override
    public RegulationsEventType eventType() {
        return RegulationsEventType.QUESTIONARY_REGISTERED;
    }

    public QuestionaryRegisteredEvent(DocumentTypeId documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public DocumentTypeId documentTypeId() { return documentTypeId; }

}
