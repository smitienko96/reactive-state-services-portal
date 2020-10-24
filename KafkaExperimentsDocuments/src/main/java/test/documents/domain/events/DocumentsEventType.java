package test.documents.domain.events;

import test.common.domain.EventType;

public enum DocumentsEventType implements EventType {

    DOCUMENT_TYPE_CREATED("Создан новый тип документа"),
    DOCUMENT_FIELD_REGISTERED("Зарегистрированы поля документа"),
    DOCUMENT_FIELD_UNREGISTERED("Исключены поля документа"),
    DOCUMENT_FIELD_OPTION_ADDED("Добавлена опция поля документа"),

    DOCUMENT_CREATED("Создан новый документ"),
    DOCUMENT_FIELDS_FILLED("Заполнены поля документа");

    private String description;

    DocumentsEventType(String description) {
        this.description = description;
    }

    @Override
    public java.lang.String description() {
        return description;
    }
}
