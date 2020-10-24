package test.requests.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class INN extends ApplicantId {

    public enum Type {
        INDIVIDUAL, COMPANY, AUTHORITY;
    }

    private Type type;

    public INN(String inn) {
        super(validateAndCorrect(inn));
        this.type = calculateType(inn);
    }

    public String innValue() { return id(); }

    public Type innType() { return type; }

    private static String validateAndCorrect(String inn) {
        return inn;
    }

    private Type calculateType(String inn) {
        // TODO: calculate based on pattern
        return Type.COMPANY;
    }

}
