package test.regulations.domain;

import test.common.domain.AbstractMementoBuilder;
import test.common.domain.DocumentTypeCode;
import test.common.domain.EventPublisher;
import test.common.domain.Memento;

import java.util.*;
import java.util.stream.Collectors;

public class RegulationConditionOptionMementoBuilder extends AbstractMementoBuilder<ConditionOption> {

    private static final String DOCUMENT_TYPES_DELIMITER = ";";

    public static final String CODE_FIELD = "code";
    public static final String VALUE_FIELD = "value";
    public static final String ORDER_FIELD = "order";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String DOCUMENT_TYPES_FIELD = "documentTypes";

    public RegulationConditionOptionMementoBuilder(EventPublisher eventPublisher) {
        super(eventPublisher);
    }

    public Memento backup(ConditionOption aggregate, List<DocumentTypeCode> documentTypes) {
        Map<String, Object> properties = new HashMap();
        this.fillBackupWithDocumentTypes(aggregate, properties, documentTypes);
        return new Memento(properties);
    }

    private void fillBackupWithDocumentTypes(ConditionOption aggregate, Map<String, Object> properties, List<DocumentTypeCode> documentTypes) {
        fillBackupProperties(aggregate, properties);

        StringJoiner joiner = new StringJoiner(DOCUMENT_TYPES_DELIMITER);
        documentTypes.forEach(t -> joiner.add(t.code()));
        properties.put(DOCUMENT_TYPES_FIELD, joiner.toString());
    }

    @Override
    protected void fillBackupProperties(ConditionOption aggregate, Map<String, Object> properties) {
        properties.put(CODE_FIELD, aggregate.code().code());
        properties.put(VALUE_FIELD, aggregate.value());
        properties.put(ORDER_FIELD, aggregate.order());
        properties.put(DESCRIPTION_FIELD, aggregate.description());
    }

    @Override
    protected ConditionOption restoreFromProperties(Map<String, Object> properties) {
        String code = getStringValue(CODE_FIELD, properties);
        String value = getStringValue(VALUE_FIELD, properties);
        int order = getIntegerValue(ORDER_FIELD, properties);
        String description = getStringValue(DESCRIPTION_FIELD, properties);

        return new ConditionOption(order, new ConditionOptionCode(code), value, description);
    }

    List<DocumentTypeCode> restoreDocumentTypes(Map<String, Object> properties) {
        String documentTypesString = getStringValue(DOCUMENT_TYPES_FIELD, properties);
        if (documentTypesString == null) {
            return null;
        }

        return Arrays.stream(documentTypesString.split(DOCUMENT_TYPES_DELIMITER))
                .map(t -> new DocumentTypeCode(t))
                .collect(Collectors.toList());
    }
}
