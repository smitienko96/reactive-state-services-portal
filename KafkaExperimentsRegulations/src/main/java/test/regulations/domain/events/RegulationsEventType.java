package test.regulations.domain.events;

import test.common.domain.EventType;

public enum RegulationsEventType implements EventType {

    REGULATION_REGISTERED("Регламент зарегистрирован"),
    REGULATION_STATUS_CHANGED("Статус регламента изменен"),
    QUESTIONARY_REGISTERED("Анкета зарегистрирована"),
    REGULATION_CANCELLED("Регламент отменен"),
    CONDITION_ADDED("Добавлено условие оказания"),
    CONDITION_OPTION_ADDED("Добавлено значение условия оказания");

    private String description;

    RegulationsEventType(String description) {
        this.description = description;
    }

    @Override
    public String description() {
        return description;
    }
}
