package test.requests.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class SNILS extends ApplicantId {

    public SNILS(String snils) {
        super(validateAndCorrect(snils));
    }

    private static String validateAndCorrect(String snils) {
        return snils;
    }

    public String snilsValue() { return id(); }
}
