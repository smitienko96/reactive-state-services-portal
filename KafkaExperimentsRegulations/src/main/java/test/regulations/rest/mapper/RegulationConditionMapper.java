package test.regulations.rest.mapper;

import test.regulations.domain.Condition;
import test.regulations.domain.ConditionOptionCode;
import test.regulations.domain.ConditionOptions;
import test.regulations.domain.published.PublishedRegulationCondition;
import test.regulations.domain.published.PublishedRegulationConditionOption;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public class RegulationConditionMapper {

    private RegulationConditionMapper() {
    }

    /**
     *
     * @param condition
     * @return
     */
    public static PublishedRegulationCondition toPublished(Condition condition) {
        ConditionOptions options = condition.options();
        List<PublishedRegulationConditionOption> publishedOptions = options.optionsSet()
                .stream().map(o -> new PublishedRegulationConditionOption(o.code().code(), o.value(),
                        o.description(), getDocumentTypeCodesForOption(options, o.code())))
                .collect(Collectors.toList());
        String conditionName = condition.definition() != null ? condition.definition().name() : null;
        String conditionDescription = condition.definition() != null ? condition.definition().description() : null;

        return new PublishedRegulationCondition(condition.code().value(), conditionName, conditionDescription, publishedOptions);

    }

    private static List<String> getDocumentTypeCodesForOption(ConditionOptions options, ConditionOptionCode code) {
        return options.documentTypesForCode(code).stream().map(c -> c.code()).collect(Collectors.toList());
    }
}
