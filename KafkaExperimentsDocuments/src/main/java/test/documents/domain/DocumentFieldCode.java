package test.documents.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(of = {"code"})
@NoArgsConstructor
public class DocumentFieldCode {
    private String code;
    private String classifierCode;

    public DocumentFieldCode(String code) {
        this.code = code;
    }

    public DocumentFieldCode(String code, String classifierCode) {
        this.code = code.toUpperCase();
        this.classifierCode =
                classifierCode != null ? classifierCode.toUpperCase() : null;
    }

    public String code() {
        return code;
    }

    public String classifierCode() {
        return classifierCode;
    }

    public String value() {
        return classifierCode != null ? classifierCode : code;
    }
}
