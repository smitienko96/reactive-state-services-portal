package test.common.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
public class DocumentTypeCode {

    private String code;

    public DocumentTypeCode(String code) {
        this.code = code;
    }

    public String code() { return code; }
}
