package test.documents.domain;

import test.common.domain.AbstractMementoBuilder;
import test.common.domain.EventPublisher;
import test.common.domain.Memento;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author s.smitienko
 */
public class DocumentFieldDefinitionMementoBuilder extends AbstractMementoBuilder<DocumentFieldDefinition> {

    public static final String CODE_FIELD = "code";
    public static final String CLASSIFIER_CODE_FIELD = "classifierCode";
    public static final String NAME_FIELD = "name";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String TYPE_FIELD = "type";
    public static final String REQUIRED_FIELD = "required";
    public static final String OPTIONS_FIELDS = "options";

    private DocumentType documentType;
    private OptionsMementoBuilder optionsBuilder;

    public DocumentFieldDefinitionMementoBuilder(DocumentType documentType,
                                                 EventPublisher eventPublisher) {
        super(eventPublisher);
        this.documentType = documentType;
        this.optionsBuilder = new OptionsMementoBuilder(eventPublisher);
    }

    @Override
    protected void fillBackupProperties(DocumentFieldDefinition aggregate, Map<String,
            Object> properties) {
        properties.put(CODE_FIELD, aggregate.fieldCode().code());
        properties.put(CLASSIFIER_CODE_FIELD,
                aggregate.fieldCode().classifierCode());
        properties.put(TYPE_FIELD, aggregate.fieldType().name());
        properties.put(NAME_FIELD, aggregate.metadata().name());
        properties.put(DESCRIPTION_FIELD, aggregate.metadata().description());
        properties.put(REQUIRED_FIELD, aggregate.isRequired());

        if (DocumentFieldType.OPTIONS == aggregate.fieldType()) {
            aggregate.options().options().forEach(o -> {
                Memento optionsMemento = optionsBuilder.backup(o);
                addToList(OPTIONS_FIELDS, optionsMemento.getState(), properties);
            });
        }

    }

    @Override
    protected DocumentFieldDefinition restoreFromProperties(Map<String, Object> properties) {
        String code = getStringValue(CODE_FIELD, properties);
        String classifierCode = getStringValue(CLASSIFIER_CODE_FIELD,
                properties);
        DocumentFieldType type =
                DocumentFieldType.valueOf(getStringValue(TYPE_FIELD, properties));
        String name = getStringValue(NAME_FIELD, properties);
        String description = getStringValue(DESCRIPTION_FIELD, properties);
        boolean required = getBooleanValue(REQUIRED_FIELD,
                properties);

        DocumentFieldDefinition field = new DocumentFieldDefinition(documentType,
                new DocumentFieldCode(code, classifierCode),
                type, required, new DocumentArtifactMetadata(name, description));

        if (DocumentFieldType.OPTIONS == type) {
            List<Map<String, Object>> options = getListOfValues(OPTIONS_FIELDS,
                    properties);
            List<DocumentFieldOption> fieldOptions =
                    options.stream().map(m -> optionsBuilder.restoreFromProperties(m)).collect(Collectors.toList());
            fieldOptions.forEach(o -> field.options().addOption(o));
        }

        return field;
    }
}
