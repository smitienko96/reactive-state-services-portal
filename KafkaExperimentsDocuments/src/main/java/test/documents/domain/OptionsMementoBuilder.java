package test.documents.domain;

import test.common.domain.AbstractMementoBuilder;
import test.common.domain.EventPublisher;

import java.util.Map;

/**
 * @author s.smitienko
 */
public class OptionsMementoBuilder extends AbstractMementoBuilder<DocumentFieldOption> {

    public static final String ORDER_FIELD = "order";
    public static final String VALUE_FIELD = "value";
    public static final String DESCRIPTION_FIELD = "description";

    public OptionsMementoBuilder(EventPublisher eventPublisher) {
        super(eventPublisher);
    }

    @Override
    protected void fillBackupProperties(DocumentFieldOption aggregate, Map<String, Object> properties) {
        properties.put(ORDER_FIELD, String.valueOf(aggregate.order()));
        properties.put(VALUE_FIELD, aggregate.value());
        properties.put(DESCRIPTION_FIELD, aggregate.description());
    }

    @Override
    protected DocumentFieldOption restoreFromProperties(Map<String, Object> properties) {
        int order = Integer.parseInt(getStringValue(ORDER_FIELD, properties));
        String value = getStringValue(VALUE_FIELD, properties);
        String description = getStringValue(DESCRIPTION_FIELD, properties);
        return new DocumentFieldOption(order, value, description);
    }
}
