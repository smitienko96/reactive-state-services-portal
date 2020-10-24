package test.documents.domain;

import test.common.domain.DocumentTypeId;
import test.common.domain.DomainEntity;
import test.documents.domain.events.DocumentFieldOptionAddedEvent;

import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Set;

public class DocumentFieldDefinition extends DomainEntity<DocumentType> {

    private DocumentFieldCode fieldCode;
    private DocumentFieldType fieldType;

    private boolean required;

    private DocumentFieldOptions options;

    private DocumentArtifactMetadata metadata;

    DocumentFieldDefinition(DocumentType type,
                            DocumentFieldCode fieldCode,
                            DocumentFieldType fieldType,
                            boolean required,
                            DocumentArtifactMetadata metadata) {
        super(type);
        this.fieldCode = fieldCode;
        this.fieldType = fieldType;
        this.required = required;
        this.metadata = metadata;
    }

    DocumentFieldDefinition(DocumentType type, DocumentFieldCode fieldCode,
                            boolean required,
                            DocumentArtifactMetadata metadata,
                            Set<String> options) {
        this(type, fieldCode, DocumentFieldType.OPTIONS, required, metadata);
        this.options = new DocumentFieldOptions(options);
    }

    public void addOption(DocumentFieldOption fieldOption) {
        if (DocumentFieldType.OPTIONS != fieldType) {
            throw new IllegalArgumentException("Unable to add option to non OPTIONS field");
        }

        raiseEvent(new DocumentFieldOptionAddedEvent(fieldCode.code(),
                fieldOption.order(),
                fieldOption.value(), fieldOption.description()));
    }

    /**
     * Проверяет является ли значение валидным кандидатом значения поля
     *
     * @param fieldValue
     * @return
     */
    public boolean isValueCandadateValid(String fieldValue) {
        if (fieldType == DocumentFieldType.STRING) {
            return true;
        }
        if (fieldType == DocumentFieldType.NUMBER || fieldType == DocumentFieldType.DATE) {
            return checkValueParseable(fieldValue);
        } else if (fieldType == DocumentFieldType.OPTIONS) {
            return options.isValueIncluded(fieldValue);
        }

        return true;
    }

    private boolean checkValueParseable(String fieldValue) {
        Format format = null;
        if (fieldType == DocumentFieldType.NUMBER) {
            format = NumberFormat.getInstance();
        } else if (fieldType == DocumentFieldType.DATE) {
            format = DateFormat.getInstance();
        } else {
            throw new IllegalStateException("This method only support parsing" +
                    " for number and date fields");
        }

        try {
            format.parseObject(fieldValue);
        } catch (ParseException ex) {
            return false;
        }
        return true;
    }

    /**
     * Возвращает идентификатор связанного типа документов
     * @return
     */
    public DocumentTypeId documentTypeId() {
        return getAggregate().identifier();
    }

    /**
     *
     * @return
     */
    public DocumentTypeCode documentTypeCode() {
        return getAggregate().documentTypeCode();
    }

    /**
     * Возвращает код поля
     * @return
     */
    public DocumentFieldCode fieldCode() {
        return fieldCode;
    }

    /**
     *
     * @return
     */
    public DocumentFieldType fieldType() {
        return fieldType;
    }

    public boolean isRequired() {
        return required;
    }

    public DocumentFieldOptions options() {
        if (DocumentFieldType.OPTIONS != fieldType) {
            throw new IllegalArgumentException("Options is not available for this field type");
        }
        if (options == null) {
            options = new DocumentFieldOptions();
        }
        return options;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public DocumentArtifactMetadata metadata() {
        return metadata;
    }
}
