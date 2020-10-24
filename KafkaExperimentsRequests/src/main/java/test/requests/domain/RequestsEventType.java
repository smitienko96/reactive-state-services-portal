package test.requests.domain;

import test.common.domain.EventType;

public enum RequestsEventType implements EventType {
    // заявители
    INDIVIDUAL_REGISTERED("Заявитель - физическое лицо зарегистрирован"),
    PASSPORT_DATA_SPECIFIED("Указаны паспортные данные"),
    INDIVIDUAL_ACCOUNT_ACTIVATED("Учетная запись заявителя активирована"),
    ORGANIZATION_REGISTERED("Заявитель - организация зарегистрирован"),

    INDIVIDUAL_UNREGISTERED("Регистрация заявителя - физического лица аннулирована"),
    ORGANIZATION_UNREGISTERED("Регистрация заявителя - организации аннулирована"),

    // заявления
    DRAFT_HANDED_OVER("Выдан бланк заявления"),
    DOCUMENT_LINKED("К заявлению прикреплен документ"),
    DOCUMENT_REJECTED("Неудачная попытка прикрепить документ к заявлению"),
    FIELDS_FILLED("Заполнены поля заявления"),
    STATUS_CHANGED("Состояние заявления изменено"),
    APPLICATION_REGISTERED("Заявление зарегистрировано"),
    APPLICATION_ARCHIVED("Заявление сдано в архив");

    private String description;

    RequestsEventType(String description) {
        this.description = description;
    }

    @Override
    public java.lang.String description() {
        return description;
    }
}
