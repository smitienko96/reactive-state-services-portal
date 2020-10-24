package test.documents.domain;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DocumentReferenceNumber {
    private String number;

    public DocumentReferenceNumber(String number) {
        this.number = number;
    }

    public String number() {
        return number;
    }
}
