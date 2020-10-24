package test.regulations.domain;

import test.common.domain.AbstractMementoBuilder;
import test.common.domain.DocumentTypeCode;
import test.common.domain.EventPublisher;
import test.common.domain.Memento;

import java.util.List;
import java.util.Map;

public class RegulationConditionMementoBuilder extends AbstractMementoBuilder<Condition> {

    public static final String CODE_FIELD = "code";
    public static final String NAME_FIELD = "name";
    public static final String ORDER_FIELD = "order";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String OPTIONS_FIELDS = "options";

    private Regulation regulation;

    private RegulationConditionOptionMementoBuilder optionsBuilder;

    public RegulationConditionMementoBuilder(Regulation regulation, EventPublisher eventPublisher) {
        super(eventPublisher);
        this.regulation = regulation;
        this.optionsBuilder = new RegulationConditionOptionMementoBuilder(eventPublisher);
    }

    @Override
    protected void fillBackupProperties(Condition aggregate, Map<String, Object> properties) {
        properties.put(CODE_FIELD, aggregate.code().value());
        properties.put(ORDER_FIELD, aggregate.order());

        if (aggregate.definition() != null) {
            properties.put(NAME_FIELD, aggregate.definition().name());
            properties.put(DESCRIPTION_FIELD, aggregate.definition().description());
        }

        ConditionOptions options = aggregate.options();
        if (options != null) {
            options.optionsSet().forEach(o -> {
                Memento optionsMemento = optionsBuilder.backup(o, options.documentTypesForCode(o.code()));
                addToList(OPTIONS_FIELDS, optionsMemento.getState(), properties);
            });
        }
    }

    @Override
    protected Condition restoreFromProperties(Map<String, Object> properties) {
        String code = getStringValue(CODE_FIELD, properties);

        String name = getStringValue(NAME_FIELD, properties);
        String description = getStringValue(DESCRIPTION_FIELD, properties);

        ConditionDefinition definition = name != null ? new ConditionDefinition(name, description) : null;

        int order = getIntegerValue(ORDER_FIELD, properties);


        List<Map<String, Object>> optionsList = getListOfValues(OPTIONS_FIELDS, properties);
        ConditionOptions options = new ConditionOptions();

        optionsList.stream().forEach(m -> {
            ConditionOption option = optionsBuilder.restoreFromProperties(m);
            List<DocumentTypeCode> documentTypes = optionsBuilder.restoreDocumentTypes(m);
            options.addOption(option, documentTypes);
        });

        return new Condition(regulation, order,
                new ConditionCode(code),
                definition,
                options);

    }

}
