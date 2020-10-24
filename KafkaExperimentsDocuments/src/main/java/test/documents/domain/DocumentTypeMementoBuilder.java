package test.documents.domain;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import test.common.domain.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author s.smitienko
 */
@Component
public class DocumentTypeMementoBuilder extends AbstractMementoBuilder<DocumentType> implements IAggregateMementoBuilder<DocumentType> {

    public static final String ID_FIELD = "id";
    public static final String VERSION_FIELD = "version";
    public static final String CODE_FIELD = "code";
    public static final String CLASSIFIER_CODE_FIELD = "classifierCode";
    public static final String STATUS_FIELD = "status";
    public static final String NAME_FIELD = "name";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String ATTACHMENT_POLICY_FIELD = "attachmentPolicy";
    public static final String DOCUMENT_FIELDS = "documentFields";
    public static final String USAGE_TYPE = "usageType";


    public DocumentTypeMementoBuilder(EventPublisher eventPublisher) {
        super(eventPublisher);
    }

    @Override
    protected void fillBackupProperties(DocumentType documentType, Map<String
            , Object> properties) {
        properties.put(ID_FIELD, documentType.identifier().id());
        properties.put(VERSION_FIELD, documentType.version().version());
        properties.put(CODE_FIELD, documentType.documentTypeCode().code());
        properties.put(CLASSIFIER_CODE_FIELD,
                documentType.documentTypeCode().classifierCode());
        properties.put(STATUS_FIELD, documentType.status().name());
        properties.put(NAME_FIELD, documentType.metadata().name());
        properties.put(DESCRIPTION_FIELD, documentType.metadata().description());
        properties.put(ATTACHMENT_POLICY_FIELD,
                documentType.attachmentPolicy().name());
        properties.put(USAGE_TYPE, documentType.usageType().name());

        DocumentFieldDefinitionMementoBuilder fieldMementoBuilder =
                new DocumentFieldDefinitionMementoBuilder(documentType, getPublisher());

        Collection<DocumentFieldDefinition> fields = documentType.getAllDocumentFields();

        fields.forEach(f -> {
            Map<String, Object> fieldProps = new HashMap<>();
            fieldMementoBuilder.fillBackupProperties(f, fieldProps);
            addToList(DOCUMENT_FIELDS, fieldProps, properties);
        });
    }

    @Override
    protected DocumentType restoreFromProperties(Map<String, Object> properties) {
        String id = getStringValue(ID_FIELD, properties);
        Integer version = getIntegerValue(VERSION_FIELD, properties);

        String code = getStringValue(CODE_FIELD, properties);
        String classifierCode = getStringValue(CLASSIFIER_CODE_FIELD, properties);
        String status = getStringValue(STATUS_FIELD, properties);
        String name = getStringValue(NAME_FIELD, properties);
        String description = getStringValue(DESCRIPTION_FIELD, properties);
        AttachmentPolicy attachmentPolicy =
                AttachmentPolicy.valueOf(getStringValue(ATTACHMENT_POLICY_FIELD, properties));
        String usageTypeValue = getStringValue(USAGE_TYPE, properties);

        UsageType usageType = usageTypeValue != null ? UsageType.valueOf(usageTypeValue) : null;

        DocumentType type = new DocumentType(getPublisher(),
                new DocumentTypeId(id),
                new AggregateVersion(version),
                new DocumentTypeCode(code, classifierCode),
                new DocumentArtifactMetadata(name, description),
                DocumentType.Status.valueOf(status), attachmentPolicy, usageType);

        DocumentFieldDefinitionMementoBuilder fieldMementoBuilder =
                new DocumentFieldDefinitionMementoBuilder(type, getPublisher());

        List<Map<String, Object>> fields = getListOfValues(DOCUMENT_FIELDS,
                properties);

        if (!CollectionUtils.isEmpty(fields)) {
            fields.forEach(f -> {
                DocumentFieldDefinition field = fieldMementoBuilder.restoreFromProperties(f);
                type.addDocumentField(field);
            });
        }

        return type;
    }

    @Override
    public String getIdentifierKey() {
        return ID_FIELD;
    }

    @Override
    public String getVersionKey() {
        return VERSION_FIELD;
    }
}
