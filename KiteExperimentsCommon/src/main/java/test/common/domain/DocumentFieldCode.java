package test.common.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(of = {"code"})
@NoArgsConstructor
public class DocumentFieldCode {
    private String code;

    public DocumentFieldCode(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }


}
