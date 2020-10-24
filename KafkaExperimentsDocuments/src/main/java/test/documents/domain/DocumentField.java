package test.documents.domain;

import test.common.domain.DomainEntity;

/**
 * @author s.smitienko
 */
public class DocumentField extends DomainEntity<Document> {


    private DocumentFieldCode fieldCode;
    private String fieldValue;

    DocumentField(Document aggregate, DocumentFieldCode fieldCode,
                  String fieldValue) {
        super(aggregate);
        this.fieldCode = fieldCode;
        this.fieldValue = fieldValue;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public DocumentFieldCode fieldCode() {
        return fieldCode;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public String fieldValue() {
        return fieldValue;
    }

}
