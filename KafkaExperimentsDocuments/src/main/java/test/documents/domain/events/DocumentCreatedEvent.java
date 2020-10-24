package test.documents.domain.events;

import lombok.Getter;
import lombok.Setter;
import test.common.domain.AggregateCreatedEvent;
import test.common.domain.AggregateNames;
import test.common.domain.DocumentId;
import test.common.domain.DocumentTypeId;
import test.documents.domain.DocumentArtifactMetadata;
import test.documents.domain.DocumentReferenceNumber;
import test.documents.domain.UsageType;

/**
 * @author s.smitienko
 */
@Setter
@Getter
public class DocumentCreatedEvent extends AggregateCreatedEvent<DocumentId, DocumentsEventType> {

    private DocumentTypeId documentTypeId;
    private DocumentArtifactMetadata metadata;
    private UsageType usageType;
    private DocumentReferenceNumber referenceNumber;

    public DocumentCreatedEvent(DocumentTypeId documentTypeId,
                                DocumentArtifactMetadata metadata, UsageType usageType,
                                DocumentReferenceNumber referenceNumber) {
        this.documentTypeId = documentTypeId;
        this.metadata = metadata;
        this.usageType = usageType;
        this.referenceNumber = referenceNumber;
    }

    public DocumentCreatedEvent(long date, String uuid, DocumentId identifier
            , DocumentTypeId documentTypeId,
                                DocumentArtifactMetadata metadata, UsageType usageType,
                                DocumentReferenceNumber referenceNumber) {
        super(date, uuid, identifier);
        this.documentTypeId = documentTypeId;
        this.metadata = metadata;
        this.usageType = usageType;
        this.referenceNumber = referenceNumber;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public DocumentTypeId documentTypeId() {
        return documentTypeId;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public DocumentArtifactMetadata metadata() {
        return metadata;
    }

    public UsageType usageType() { return usageType; }

    public DocumentReferenceNumber referenceNumber() { return referenceNumber; }

    @Override
    public String aggregateName() {
        return AggregateNames.DOCUMENTS;
    }

    @Override
    public DocumentsEventType eventType() {
        return DocumentsEventType.DOCUMENT_CREATED;
    }
}
