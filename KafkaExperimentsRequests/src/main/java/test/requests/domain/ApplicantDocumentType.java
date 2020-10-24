package test.requests.domain;

import lombok.NoArgsConstructor;
import test.common.domain.DocumentTypeCode;
import test.common.domain.DocumentTypeId;

@NoArgsConstructor
public class ApplicantDocumentType {

    private DocumentTypeCode typeCode;
    private DocumentTypeId typeId;

    public ApplicantDocumentType(DocumentTypeCode typeCode, DocumentTypeId typeId) {
        this.typeCode = typeCode;
        this.typeId = typeId;
    }

    public DocumentTypeCode typeCode() {
        return typeCode;
    }

    public DocumentTypeId typeId() { return typeId; }
}
