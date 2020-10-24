package test.documents.domain.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import test.common.domain.AggregateCreatedEvent;
import test.common.domain.AggregateNames;
import test.common.domain.DocumentTypeId;
import test.documents.domain.AttachmentPolicy;
import test.documents.domain.DocumentArtifactMetadata;
import test.documents.domain.DocumentTypeCode;
import test.documents.domain.UsageType;

@Setter
@Getter
@NoArgsConstructor
public class DocumentTypeCreatedEvent extends AggregateCreatedEvent<DocumentTypeId, DocumentsEventType> {

    private DocumentTypeCode documentTypeCode;
    private DocumentArtifactMetadata metadata;

    private AttachmentPolicy attachmentPolicy;
    private UsageType usageType;

    public DocumentTypeCreatedEvent(DocumentTypeCode documentTypeCode,
                                    DocumentArtifactMetadata metadata,
                                    AttachmentPolicy attachmentPolicy,
                                    UsageType usageType) {
        this.documentTypeCode = documentTypeCode;
        this.metadata = metadata;
        this.attachmentPolicy = attachmentPolicy;
        this.usageType = usageType;
    }

    public DocumentTypeCreatedEvent(long date, String uuid, DocumentTypeId identifier,
                                    DocumentTypeCode documentTypeCode, DocumentArtifactMetadata metadata,
                                    AttachmentPolicy attachmentPolicy,
                                    UsageType usageType) {
        super(date, uuid, identifier);
        this.documentTypeCode = documentTypeCode;
        this.metadata = metadata;
        this.attachmentPolicy = attachmentPolicy;
        this.usageType = usageType;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public DocumentTypeCode documentTypeCode() {
        return documentTypeCode;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public DocumentArtifactMetadata metadata() {
        return metadata;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public AttachmentPolicy attachmentPolicy() {
        return attachmentPolicy;
    }

    public UsageType usageType() {
        return usageType;
    }

    @Override
    public String aggregateName() {
        return AggregateNames.DOCUMENT_TYPES;
    }

    @Override
    public DocumentsEventType eventType() {
        return DocumentsEventType.DOCUMENT_TYPE_CREATED;
    }

}
