package test.requests.domain;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ApplicationType {
    private String typeCode;

    public ApplicationType(String typeCode) {
        this.typeCode = typeCode;
    }

    public String typeCode() {
        return typeCode;
    }
}
