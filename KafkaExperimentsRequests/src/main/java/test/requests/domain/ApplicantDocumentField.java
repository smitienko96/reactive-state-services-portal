package test.requests.domain;

import lombok.NoArgsConstructor;
import test.common.domain.DocumentId;

@NoArgsConstructor
public class ApplicantDocumentField {

    private DocumentId documentId;
    private DocumentFieldCode fieldCode;
    private String fieldValue;

    public ApplicantDocumentField(DocumentFieldCode fieldCode,
                                String fieldValue) {
        this.fieldCode = fieldCode;
        this.fieldValue = fieldValue;
    }

    public ApplicantDocumentField(DocumentId documentId,
                            DocumentFieldCode fieldCode, String fieldValue) {
        this(fieldCode, fieldValue);
        this.documentId = documentId;
    }

    void linkDocument(DocumentId documentId) { this.documentId = documentId; }

    void changeValue(String newValue) {
        this.fieldValue = newValue;
    }

    public DocumentId documentId() { return documentId; }

    public DocumentFieldCode fieldCode() { return fieldCode; }

    public String fieldValue() { return fieldValue; }
}
