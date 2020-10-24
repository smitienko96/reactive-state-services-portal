package test.documents.domain.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import test.common.domain.AggregateNames;
import test.common.domain.AggregateVersion;
import test.common.domain.DocumentId;
import test.common.domain.DomainEvent;
import test.documents.domain.DocumentFieldCode;

import java.util.Map;

/**
 * @author s.smitienko
 */
@Setter
@Getter
@NoArgsConstructor
public class DocumentFieldsFilledEvent extends DomainEvent<DocumentId, DocumentsEventType> {

    private Map<DocumentFieldCode, String> documentFields;

    public DocumentFieldsFilledEvent(Map<DocumentFieldCode, String> documentFields) {
        this.documentFields = documentFields;
    }

    public DocumentFieldsFilledEvent(long date, String uuid, DocumentId identifier, AggregateVersion version, Map<DocumentFieldCode, String> documentFields) {
        super(date, uuid, identifier, version);
        this.documentFields = documentFields;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public Map<DocumentFieldCode, String> documentFields() {
        return documentFields;
    }

    @Override
    public String aggregateName() {
        return AggregateNames.DOCUMENTS;
    }

    @Override
    public DocumentsEventType eventType() {
        return DocumentsEventType.DOCUMENT_FIELDS_FILLED;
    }
}
