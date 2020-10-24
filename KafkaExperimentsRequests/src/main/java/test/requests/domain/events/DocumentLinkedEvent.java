package test.requests.domain.events;

import test.common.domain.*;
import test.requests.domain.ApplicantDocumentType;
import test.requests.domain.ApplicationNumber;
import test.requests.domain.DocumentFieldCode;
import test.requests.domain.RequestsEventType;

import java.util.Map;

public class DocumentLinkedEvent extends DomainEvent<ApplicationNumber, RequestsEventType> {

    private DocumentId documentId;
    private String name;
    private String description;

    private String designation;

    private ApplicantDocumentType documentType;

    private Map<DocumentFieldCode, String> documentFields;

    public DocumentLinkedEvent(long date, String uuid,
                               ApplicationNumber identifier,
                               AggregateVersion version,
                               DocumentId documentId,
                               ApplicantDocumentType documentType,
                               String name,
                               String description,
                               String designation,
                               Map<DocumentFieldCode,
                               String> documentFields) {
        super(date, uuid, identifier, version);
        this.documentId = documentId;
        this.documentType = documentType;
        this.name = name;
        this.description = description;
        this.designation = designation;
        this.documentFields = documentFields;
    }

    public DocumentLinkedEvent(DocumentId documentId,
                               ApplicantDocumentType documentType,
                               String name,
                               String description,
                               String designation,
                               Map<DocumentFieldCode, String> documentFields) {
        this.documentId = documentId;
        this.documentType = documentType;
        this.name = name;
        this.description = description;
        this.designation = designation;
        this.documentFields = documentFields;
    }

    public DocumentId documentId() {
        return documentId;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public ApplicantDocumentType documentType() {
        return documentType;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public String name() {
        return name;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public String description() {
        return description;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public String designation() {
        return designation;
    }


    public Map<DocumentFieldCode, String> documentFields() {
        return documentFields;
    }

    @Override
    public String aggregateName() {
        return AggregateNames.APPLICATIONS;
    }

    @Override
    public RequestsEventType eventType() {
        return RequestsEventType.DOCUMENT_LINKED;
    }

}
