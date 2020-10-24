package test.regulations.domain;

import test.common.domain.DocumentTypeCode;
import test.common.domain.DomainEntity;
import test.regulations.domain.events.RegulationConditionOptionAddedEvent;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Arrays;
import java.util.List;

public class Condition extends DomainEntity<Regulation> {

    private int order;

    private ConditionCode code;
    private ConditionDefinition definition;
    private ConditionOptions options;

    public Condition(Regulation aggregate, int order, ConditionCode code, ConditionDefinition definition, ConditionOptions options) {
        super(aggregate);
        this.order = order;
        this.code = code;
        this.definition = definition;
        this.options = options;
    }

    Condition(Regulation regulation, int order, ConditionCode code,
              ConditionDefinition definition) {
        super(regulation);
        this.order = order;
        this.code = code;
        this.definition = definition;
        this.options = new ConditionOptions();
    }



    Condition(Regulation regulation, ConditionCode code) {
        this(regulation, 0, code, null);
    }

    /**
     *
     * @param option
     * @param typeCode
     */
    public void addOption(ConditionOption option, DocumentTypeCode typeCode) {
        addOption(option, Arrays.asList(typeCode));
    }

    /**
     *
     * @param option
     * @param typeCodes
     */
    public void addOption(ConditionOption option,
                           List<DocumentTypeCode> typeCodes) {
        raiseEvent(new RegulationConditionOptionAddedEvent(code, option.code(),
                option.order(), option.value(), option.description(),
                typeCodes));

    }

    /**
     *
     * @param order
     * @param definition
     */
    void changeDefinition(@PositiveOrZero int order, @NotNull ConditionDefinition definition) {
        this.order = order;
        this.definition = definition;
    }

    public boolean isComplete() {
        return options.isComplete();
    }

    public int order() { return order; }

    public ConditionCode code() { return code; }

    public ConditionDefinition definition() { return definition; }

    public ConditionOptions options() { return options; }
}
