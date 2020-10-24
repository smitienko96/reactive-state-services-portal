package test.documents.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import test.common.domain.DocumentTypeId;

@ToString
@EqualsAndHashCode(of = {"code"})
@NoArgsConstructor
public class DocumentTypeCode {

    private String code;
    private String classifierCode;

    public DocumentTypeCode(DocumentTypeId typeId) {
        this(typeId.id());
    }

    public DocumentTypeCode(String code, String classifierCode) {
        this.code = code;
        this.classifierCode = classifierCode;
    }

    public DocumentTypeCode(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }

    public String classifierCode() {
        return classifierCode;
    }


}
