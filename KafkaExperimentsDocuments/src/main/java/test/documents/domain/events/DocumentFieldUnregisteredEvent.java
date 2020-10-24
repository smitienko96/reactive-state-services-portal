package test.documents.domain.events;

import lombok.Getter;
import lombok.Setter;
import test.common.domain.AggregateNames;
import test.common.domain.AggregateVersion;
import test.common.domain.DocumentTypeId;
import test.common.domain.DomainEvent;
import test.documents.domain.DocumentFieldCode;

/**
 * @author s.smitienko
 */
@Setter
@Getter
public class DocumentFieldUnregisteredEvent extends DomainEvent<DocumentTypeId, DocumentsEventType> {

    private DocumentFieldCode fieldCode;

    public DocumentFieldUnregisteredEvent(DocumentFieldCode fieldCode) {
        this.fieldCode = fieldCode;
    }

    public DocumentFieldUnregisteredEvent(long date, String uuid, DocumentTypeId identifier, AggregateVersion version, DocumentFieldCode fieldCode) {
        super(date, uuid, identifier, version);
        this.fieldCode = fieldCode;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public DocumentFieldCode fieldCode() {
        return fieldCode;
    }

    @Override
    public String aggregateName() {
        return AggregateNames.DOCUMENT_TYPES;
    }

    @Override
    public DocumentsEventType eventType() {
        return DocumentsEventType.DOCUMENT_FIELD_UNREGISTERED;
    }
}
