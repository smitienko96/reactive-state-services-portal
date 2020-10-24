package test.regulations.domain;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = {"code"})
public class ConditionOption implements Comparable<ConditionOption> {
    private int order;

    private String value;
    private ConditionOptionCode code;
    private String description;

    public ConditionOption(int order, ConditionOptionCode code, String value) {
        this(order, code, value, "");
    }

    public ConditionOption(int order, ConditionOptionCode code, String value, String description) {
        this.order = order;
        this.code = code;
        this.value = value;
        this.description = description;
    }

    @Override
    public int compareTo(ConditionOption o) {
        return order - o.order;
    }

    public int order() {
        return order;
    }

    public ConditionOptionCode code() {
        return code;
    }

    public String value() {
        return value;
    }

    public String description() {
        return description;
    }
}
