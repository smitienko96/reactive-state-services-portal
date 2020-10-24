package test.requests.domain.events;

import test.common.domain.*;
import test.requests.domain.ApplicationNumber;
import test.requests.domain.DocumentFieldCode;
import test.requests.domain.RequestsEventType;

import java.util.Map;

public class DocumentRejectedEvent extends DomainEvent<ApplicationNumber, RequestsEventType> {

    private DocumentId documentId;
    private DocumentTypeId documentTypeId;
    Map<DocumentFieldCode, String> documentFields;

    public DocumentRejectedEvent(long date, String uuid, ApplicationNumber identifier,
                                 AggregateVersion version, DocumentId documentId,
                                 DocumentTypeId documentTypeId, Map<DocumentFieldCode, String> documentFields) {
        super(date, uuid, identifier, version);
        this.documentId = documentId;
        this.documentTypeId = documentTypeId;
        this.documentFields = documentFields;
    }

    public DocumentRejectedEvent(DocumentId documentId, DocumentTypeId documentTypeId, Map<DocumentFieldCode, String> documentFields) {
        this.documentId = documentId;
        this.documentTypeId = documentTypeId;
        this.documentFields = documentFields;
    }

    public DocumentId documentId() {
        return documentId;
    }

    @Override
    public String aggregateName() {
        return AggregateNames.APPLICATIONS;
    }

    @Override
    public RequestsEventType eventType() {
        return RequestsEventType.DOCUMENT_REJECTED;
    }

    public DocumentTypeId documentTypeId() {
        return documentTypeId;
    }

    public Map<DocumentFieldCode, String> documentFields() {
        return documentFields;
    }

}
