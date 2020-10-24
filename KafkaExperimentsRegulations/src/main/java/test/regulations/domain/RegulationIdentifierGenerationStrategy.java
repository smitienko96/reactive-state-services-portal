package test.regulations.domain;

import org.springframework.stereotype.Component;
import test.common.domain.AggregateNames;
import test.common.service.AggregateIdentifierGenerationStrategy;

import java.util.Date;
import java.util.UUID;

/**
 *
 */
@Component
public class RegulationIdentifierGenerationStrategy implements AggregateIdentifierGenerationStrategy<RegulationIdentifier> {

    @Override
    public RegulationIdentifier generate() {
        return new RegulationIdentifier(UUID.randomUUID().toString(), new Date());
    }

    @Override
    public String aggregateName() {
        return AggregateNames.REGULATIONS;
    }
}
