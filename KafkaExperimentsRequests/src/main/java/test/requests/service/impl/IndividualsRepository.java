package test.requests.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import test.common.domain.*;
import test.requests.domain.IIndividualsRepository;
import test.requests.domain.Individual;
import test.requests.domain.IndividualBuilder;
import test.requests.domain.SNILS;
import test.requests.domain.events.IndividualRegisteredEvent;
import test.requests.domain.events.IndividualUnregisteredEvent;

@Component
@Slf4j
public class IndividualsRepository extends
        ProjectingAggregateRepository<Individual, SNILS, IndividualRegisteredEvent> implements IIndividualsRepository {

    @Override
    protected Individual createInitialState(IndividualRegisteredEvent initialEvent, AggregateVersion lastVersion, EventPublisher publisher) {
        BuilderResult<Individual> result = IndividualBuilder.restore(publisher, lastVersion)
                .withSNILS(initialEvent.aggregateIdentifier())
                .withFirstName(initialEvent.firstName())
                .withLastName(initialEvent.lastName())
                .withPatronymic(initialEvent.patronymic())
                .build();
        if (result.isSaveRequired()) {
            throw new RuntimeException("Unable to restore aggregate which is not event sourced");
        }

        return result.getAggregate();
    }

    @Override
    protected IndividualUnregisteredEvent createAggregateDeletedEvent(Individual aggregate) {
        return new IndividualUnregisteredEvent(aggregate);
    }

    @Override
    protected String aggregateName() {
        return AggregateNames.INDIVIDUALS;
    }


}
