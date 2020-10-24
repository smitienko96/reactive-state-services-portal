package test.documents.domain;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import test.common.domain.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DocumentMementoBuilder extends AbstractMementoBuilder<Document> implements IAggregateMementoBuilder<Document> {

    public static final String ID_FIELD = "id";
    public static final String VERSION_FIELD = "version";
    public static final String DOCUMENT_TYPE_ID_FIELD = "documentTypeId";
    public static final String NAME_FIELD = "name";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String USAGE_TYPE = "usageType";
    public static final String REFERENCE_NUMBER_FIELD = "referenceNumber";

    public static final String DOCUMENT_FIELDS = "documentFields";

    public DocumentMementoBuilder(EventPublisher eventPublisher) {
        super(eventPublisher);
    }

    @Override
    protected void fillBackupProperties(Document document, Map<String, Object> properties) {
        properties.put(ID_FIELD, document.identifier().id());
        properties.put(VERSION_FIELD, document.version().version());
        properties.put(DOCUMENT_TYPE_ID_FIELD, document.documentType().id());
        properties.put(NAME_FIELD, document.metadata().name());
        properties.put(DESCRIPTION_FIELD, document.metadata().description());
        properties.put(USAGE_TYPE, document.usageType().name());

        if (document.referenceNumber() != null) {
            properties.put(REFERENCE_NUMBER_FIELD, document.referenceNumber().number());
        }

        DocumentFieldMementoBuilder fieldMementoBuilder =
                new DocumentFieldMementoBuilder(document, getPublisher());

        Collection<DocumentField> fields = document.allDocumentFields();

        fields.forEach(f -> {
            Map<String, Object> fieldProps = new HashMap<>();
            fieldMementoBuilder.fillBackupProperties(f, fieldProps);
            addToList(DOCUMENT_FIELDS, fieldProps, properties);
        });
    }

    @Override
    protected Document restoreFromProperties(Map<String, Object> properties) {
        String id = getStringValue(ID_FIELD, properties);
        int version = getIntegerValue(VERSION_FIELD, properties);

        String documentTypeId = getStringValue(DOCUMENT_TYPE_ID_FIELD, properties);
        String name = getStringValue(NAME_FIELD, properties);
        String description = getStringValue(DESCRIPTION_FIELD, properties);
        String usageType = getStringValue(USAGE_TYPE, properties);

        String referenceNumber = getStringValue(REFERENCE_NUMBER_FIELD, properties);

        Document document = new Document(getPublisher(), new DocumentId(id),
                new AggregateVersion(version),
                new DocumentTypeId(documentTypeId),
                new DocumentArtifactMetadata(name, description),
                UsageType.valueOf(usageType),
                referenceNumber);

        DocumentFieldMementoBuilder fieldMementoBuilder =
                new DocumentFieldMementoBuilder(document, getPublisher());

        List<Map<String, Object>> fields = getListOfValues(DOCUMENT_FIELDS,
                properties);

        if (!CollectionUtils.isEmpty(fields)) {
            fields.forEach(f -> {
                DocumentField field = fieldMementoBuilder.restoreFromProperties(f);
                document.addDocumentField(field);
            });
        }

        return document;
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
