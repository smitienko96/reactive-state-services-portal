package test.documents.domain.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import test.common.domain.AggregateNames;
import test.common.domain.AggregateVersion;
import test.common.domain.DocumentTypeId;
import test.common.domain.DomainEvent;
import test.documents.domain.DocumentArtifactMetadata;
import test.documents.domain.DocumentFieldCode;
import test.documents.domain.DocumentFieldType;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class DocumentFieldRegisteredEvent extends DomainEvent<DocumentTypeId, DocumentsEventType> {

    private DocumentFieldCode fieldCode;

    private DocumentFieldType fieldType;
    private boolean required;

    private DocumentArtifactMetadata metadata;

    private Set<String> options;

    public DocumentFieldRegisteredEvent(DocumentFieldCode fieldCode,
                                        DocumentFieldType fieldType,
                                        boolean required,
                                        DocumentArtifactMetadata metadata) {
        this.fieldCode = fieldCode;
        this.fieldType = fieldType;
        this.required = required;
        this.metadata = metadata;
    }

    public DocumentFieldRegisteredEvent(DocumentFieldCode fieldCode,
                                        DocumentFieldType fieldType,
                                        boolean required,
                                        DocumentArtifactMetadata metadata,
                                        Set<String> options) {
        this(fieldCode, fieldType, required, metadata);
        this.options = options;
    }

    public DocumentFieldRegisteredEvent(long date, String uuid, DocumentTypeId identifier,
                                        AggregateVersion version, DocumentFieldCode fieldCode,
                                        DocumentFieldType fieldType,
                                        boolean required,
                                        DocumentArtifactMetadata metadata,
                                        Set<String> options) {
        super(date, uuid, identifier, version);
        this.fieldCode = fieldCode;
        this.fieldType = fieldType;
        this.required = required;
        this.metadata = metadata;
        this.options = options;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public DocumentFieldCode fieldCode() {
        return fieldCode;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public DocumentFieldType fieldType() {
        return fieldType;
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
    public boolean isRequired() {
        return required;
    }

    public Set<String> options() {
        return options;
    }

    @Override
    public String aggregateName() {
        return AggregateNames.DOCUMENT_TYPES;
    }

    @Override
    public DocumentsEventType eventType() {
        return DocumentsEventType.DOCUMENT_FIELD_REGISTERED;
    }

}
