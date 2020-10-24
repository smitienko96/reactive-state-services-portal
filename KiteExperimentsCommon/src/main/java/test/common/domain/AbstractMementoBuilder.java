package test.common.domain;

import org.jooq.tools.StringUtils;

import java.util.*;

/**
 * @author s.smitienko
 */
public abstract class AbstractMementoBuilder<A> implements IMementoBuilder<A> {

    private EventPublisher eventPublisher;

    protected AbstractMementoBuilder(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Memento backup(A aggregate) {
        Map<String, Object> properties = new HashMap<>();

        fillBackupProperties(aggregate, properties);

        return new Memento(properties);
    }

    @Override
    public A restore(Memento memento) {
        return restoreFromProperties(memento.getState());
    }

    protected EventPublisher getPublisher() {
        return eventPublisher;
    }

    protected String getStringValue(String field,
                                  Map<String, Object> properties) {
        return (String) properties.get(field);
    }

    protected Boolean getBooleanValue(String field,
                                    Map<String, Object> properties) {
        return (Boolean) properties.get(field);
    }

    protected Integer getIntegerValue(String field,
                                      Map<String, Object> properties) {
        return (Integer) properties.get(field);
    }

    protected List<Map<String, Object>> getListOfValues(String field,
                                       Map<String, Object> properties) {

        return (List<Map<String, Object>>) properties.get(field);
    }

    protected void addToList(String listField,
                             Map<String, Object> subproperties,
                          Map<String,
                          Object> properties) {
        List<Map<String, Object>> list = getListOfValues(listField, properties);
        if (list == null) {
            list = new ArrayList<>();
            properties.put(listField, list);
        }
        list.add(subproperties);
    }

    protected String convertDateToString(Date date) {
        return String.valueOf(date.getTime());
    }

    protected Date convertDateFromString(String dateString) {
        if (StringUtils.isBlank(dateString)) {
            return null;
        }
        return new Date(Long.parseLong(dateString));
    }

    protected abstract void fillBackupProperties(A aggregate, Map<String,
            Object> properties);

    protected abstract A restoreFromProperties(Map<String, Object> properties);
}
