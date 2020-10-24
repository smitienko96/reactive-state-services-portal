package test.documents.domain;

import test.common.domain.IReadable;

/**
 * Тип использования документа
 */
public enum UsageType implements IReadable {

    EXTERNAL("Внешнее использование"),
    INTERNAL("Внутреннее использование"),
    CLASSIFIED("Ограниченный доступ");

    private String description;

    UsageType(String description) {
        this.description = description;
    }

    public String description() { return description; }
}
