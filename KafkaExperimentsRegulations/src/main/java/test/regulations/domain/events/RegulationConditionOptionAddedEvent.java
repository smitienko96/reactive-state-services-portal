package test.regulations.domain.events;

import lombok.NoArgsConstructor;
import test.common.domain.AggregateNames;
import test.common.domain.DocumentTypeCode;
import test.common.domain.DomainEvent;
import test.regulations.domain.ConditionCode;
import test.regulations.domain.ConditionOptionCode;
import test.regulations.domain.RegulationIdentifier;

import java.util.List;

/**
 * @author s.smitienko
 */
@NoArgsConstructor
public class RegulationConditionOptionAddedEvent extends DomainEvent<RegulationIdentifier, RegulationsEventType> {

    private ConditionCode conditionCode;
    private ConditionOptionCode code;

    private int order;
    private String value;
    private String description;

    private List<DocumentTypeCode> typeCodes;

    public RegulationConditionOptionAddedEvent(ConditionCode conditionCode, ConditionOptionCode code,
                                               int order, String value,
                                               String description,
                                               List<DocumentTypeCode> typeCodes) {
        this.conditionCode = conditionCode;
        this.code = code;
        this.order = order;
        this.value = value;
        this.description = description;
        this.typeCodes = typeCodes;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public ConditionCode conditionCode() {
        return conditionCode;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public ConditionOptionCode code() {
        return code;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public int order() {
        return order;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public String value() {
        return value;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public String description() {
        return description;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public List<DocumentTypeCode> typeCodes() {
        return typeCodes;
    }

    @Override
    public String aggregateName() {
        return AggregateNames.REGULATIONS;
    }

    @Override
    public RegulationsEventType eventType() {
        return RegulationsEventType.CONDITION_OPTION_ADDED;
    }



}
