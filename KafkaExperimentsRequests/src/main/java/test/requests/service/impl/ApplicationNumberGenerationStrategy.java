package test.requests.service.impl;

import org.springframework.stereotype.Component;
import test.common.domain.AggregateNames;
import test.common.service.AggregateIdentifierGenerationStrategy;
import test.requests.domain.ApplicationNumber;

import java.util.Random;

@Component
public class ApplicationNumberGenerationStrategy implements AggregateIdentifierGenerationStrategy<ApplicationNumber> {

    @Override
    public ApplicationNumber generate() {
        String value = "APP-" + new Random().nextInt(10000);
        return new ApplicationNumber(value);
    }

    @Override
    public String aggregateName() {
        return AggregateNames.APPLICATIONS;
    }

}
