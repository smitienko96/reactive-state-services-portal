package test.regulations.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
public class ConditionCode {
    private String code;

    public ConditionCode(String code) {
        this.code = code;
    }

    public String value() { return code; }
}
