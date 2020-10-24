package test.regulations.domain;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class ConditionOptionCode {
    private String code;

    public ConditionOptionCode(String code) {
        this.code = code;
    }

    public String code() { return code; }
}
