package test.documents.domain.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import test.common.domain.AggregateNames;
import test.common.domain.AggregateVersion;
import test.common.domain.DocumentTypeId;
import test.common.domain.DomainEvent;

/**
 * @author s.smitienko
 */
@Setter
@Getter
@NoArgsConstructor
public class DocumentFieldOptionAddedEvent extends DomainEvent<DocumentTypeId, DocumentsEventType> {

    private String fieldCode;

    private int order;
    private String value;
    private String description;

    public DocumentFieldOptionAddedEvent(long date, String uuid, DocumentTypeId identifier,
                                         AggregateVersion version, String fieldCode,
                                         int order, String value, String description) {
        super(date, uuid, identifier, version);
        this.fieldCode = fieldCode;
        this.order = order;
        this.value = value;
        this.description = description;
    }

    public DocumentFieldOptionAddedEvent(String fieldCode, int order, String value,
                                         String description) {
        this.fieldCode = fieldCode;
        this.order = order;
        this.value = value;
        this.description = description;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public String fieldCode() {
        return fieldCode;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public int order() {
        return order;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public String value() {
        return value;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public String description() {
        return description;
    }

    @Override
    public String aggregateName() {
        return AggregateNames.DOCUMENT_TYPES;
    }

    @Override
    public DocumentsEventType eventType() {
        return DocumentsEventType.DOCUMENT_FIELD_OPTION_ADDED;
    }
}
