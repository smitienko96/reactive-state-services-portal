package test.documents.domain;

import org.apache.commons.lang3.StringUtils;
import test.common.domain.*;
import test.documents.domain.events.DocumentCreatedEvent;
import test.documents.domain.events.DocumentFieldsFilledEvent;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class Document extends DomainAggregate<DocumentId> {

    public static final String TYPE_COMPONENTS_DELIMITER = ".";

    private DocumentTypeId documentTypeId;
    private DocumentArtifactMetadata metadata;

    private Map<DocumentFieldCode, DocumentField> fields;

    private UsageType usageType;
    private DocumentReferenceNumber referenceNumber;

    public Document(EventPublisher publisher, DocumentId identifier,
                    DocumentType documentType,
                    DocumentArtifactMetadata metadata,
                    String referenceNumber) {
        super(publisher, identifier);

        if (!documentType.isReadyToUse()) {
            throw new IllegalArgumentException(MessageFormat.format("Document" +
                    " type [{0}] is not ready to use. Check it and try again" +
                    ".", documentType.identifier().id()));
        }
        this.metadata = metadata;
        this.documentTypeId = documentType.identifier();
        this.usageType = documentType.usageType();
        this.referenceNumber = !StringUtils.isBlank(referenceNumber) ?
                new DocumentReferenceNumber(referenceNumber) : null;

        done();
    }

    public Document(EventPublisher publisher, DocumentId identifier, AggregateVersion version,
                    DocumentTypeId documentType,
                    DocumentArtifactMetadata metadata,
                    UsageType usageType,
                    String referenceNumber) {
        super(publisher, identifier, version);
        this.documentTypeId = documentType;
        this.metadata = metadata;
        this.usageType = usageType;
        this.referenceNumber = !StringUtils.isBlank(referenceNumber) ?
                new DocumentReferenceNumber(referenceNumber) : null;
    }

    /**
     * @param fillService
     * @param fieldCode
     * @param fieldValue
     * @return
     */
    public boolean fillDocumentField(DocumentFillService fillService,
                                     DocumentFieldCode fieldCode,
                                     String fieldValue) {
        Map<DocumentFieldCode, String> fieldsMap =
                Collections.singletonMap(fieldCode, fieldValue);

        return fillDocumentFields(fillService, fieldsMap);
    }

    /**
     * @param fillService
     * @param fields
     */
    public boolean fillDocumentFields(DocumentFillService fillService,
                                      Map<DocumentFieldCode, String> fields) {

        Map<DocumentFieldCode, String> fieldsToAdd = fields.entrySet().stream()
                .filter(e -> fillService.isValueSupportedForDocumentField(documentTypeId, e.getKey(), e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue));

        if (fieldsToAdd == null || fieldsToAdd.isEmpty()) {
            return false;
        }

        raiseEvent(new DocumentFieldsFilledEvent(fieldsToAdd));

        return true;
    }

    /**
     * @param fieldsFilled
     */
    public void on(DocumentFieldsFilledEvent fieldsFilled) {
        Map<DocumentFieldCode, DocumentField> fieldsToAdd =
                fieldsFilled.documentFields().entrySet().stream()
                        .map(e -> new DocumentField(this, e.getKey(), e.getValue()))
                        .collect(Collectors.toMap(DocumentField::fieldCode, e -> e));

        fields.putAll(fieldsToAdd);
    }

    void addDocumentField(DocumentField field) {
        fields.put(field.fieldCode(), field);
    }

    public Collection<DocumentField> allDocumentFields() {
        return fields.values();
    }

    public DocumentArtifactMetadata metadata() {
        return metadata;
    }

    public DocumentTypeId documentType() {
        return documentTypeId;
    }

    public UsageType usageType() {
        return usageType;
    }

    public DocumentReferenceNumber referenceNumber() {
        return referenceNumber;
    }

    @Override
    public DocumentCreatedEvent createInitialEvent() {
        return new DocumentCreatedEvent(documentTypeId, metadata, usageType, referenceNumber);
    }
}
