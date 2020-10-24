package test.requests.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import test.common.domain.DocumentTypeCode;

import java.util.StringJoiner;

@EqualsAndHashCode
@NoArgsConstructor
public class DocumentFieldCode {

    private static final String SPLIT_CHARACTER = ".";
    private DocumentTypeCode documentTypeCode;
    private String code;

    public DocumentFieldCode(String code) {
        String doctypeCode = StringUtils.substringBeforeLast(code, SPLIT_CHARACTER);
        String docfieldCode = StringUtils.substringAfterLast(code, SPLIT_CHARACTER);
        if (StringUtils.isEmpty(doctypeCode) || StringUtils.isEmpty(docfieldCode)) {
            throw new RuntimeException("Field should contain at least 2 components");
        }
        this.documentTypeCode = new DocumentTypeCode(doctypeCode);
        this.code = docfieldCode;
    }

    public DocumentTypeCode documentTypeCode() {
        return documentTypeCode;
    }

    public String code() { return code; }

    public String flatCode() {
        return new StringJoiner(SPLIT_CHARACTER)
                .add(documentTypeCode().code())
                .add(code()).toString();
    }
}
