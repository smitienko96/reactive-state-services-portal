package test.documents.domain;

import test.common.domain.AbstractMementoBuilder;
import test.common.domain.EventPublisher;

import java.util.Map;

public class DocumentFieldMementoBuilder extends AbstractMementoBuilder<DocumentField> {

    public static final String CODE_FIELD = "code";
    public static final String CLASSIFIER_CODE_FIELD = "classifierCode";
    public static final String VALUE_FIELD = "value";

    private Document document;

    public DocumentFieldMementoBuilder(Document document, EventPublisher eventPublisher) {
        super(eventPublisher);
        this.document = document;
    }

    @Override
    protected void fillBackupProperties(DocumentField aggregate, Map<String, Object> properties) {
        properties.put(CODE_FIELD, aggregate.fieldCode().code());
        properties.put(CLASSIFIER_CODE_FIELD, aggregate.fieldCode().classifierCode());
        properties.put(VALUE_FIELD, aggregate.fieldValue());
    }

    @Override
    protected DocumentField restoreFromProperties(Map<String, Object> properties) {
        String code = getStringValue(CODE_FIELD, properties);
        String classifierCode = getStringValue(CLASSIFIER_CODE_FIELD, properties);
        String value = getStringValue(VALUE_FIELD, properties);

        return new DocumentField(document, new DocumentFieldCode(code, classifierCode), value);
    }

}
