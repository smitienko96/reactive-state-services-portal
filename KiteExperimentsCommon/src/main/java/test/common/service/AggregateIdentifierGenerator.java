package test.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import test.common.domain.AggregateIdentifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author s.smitienko
 */
@Component
public class AggregateIdentifierGenerator {

    private Map<String,
            AggregateIdentifierGenerationStrategy<? extends AggregateIdentifier>> strategyMap;

    /**
     * Генерирует следующее значение идентификатора для агрегата
     *
     * @param aggregateName имя агрегата предметной области
     * @param <I>
     * @return
     */
    public <I extends AggregateIdentifier> I generate(String aggregateName) {
        AggregateIdentifierGenerationStrategy<I> strategy =
                (AggregateIdentifierGenerationStrategy<I>) strategyMap.get(aggregateName);
        if (strategy == null) {
            throw new RuntimeException(String.format("No generation strategy " +
                    "found for " +
                    "aggregate [%s]", aggregateName));
        }
        return strategy.generate();
    }

    @Autowired
    public void setIdentifierGenerationStrategies(Set<AggregateIdentifierGenerationStrategy> strategies) {
        strategyMap = new HashMap<>(strategies.size());
        strategies.forEach(s -> strategyMap.put(s.aggregateName(), s));
    }

}
