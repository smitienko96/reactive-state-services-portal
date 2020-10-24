package test.requests.domain.events;

import test.common.domain.AggregateDeletedEvent;
import test.common.domain.AggregateNames;
import test.common.domain.AggregateVersion;
import test.requests.domain.RequestsEventType;
import test.requests.domain.Individual;
import test.requests.domain.SNILS;

public class IndividualUnregisteredEvent extends AggregateDeletedEvent<Individual, SNILS, RequestsEventType> {

    public IndividualUnregisteredEvent(Individual aggregate) {
        super(aggregate);
    }

    public IndividualUnregisteredEvent(long date, String uuid, SNILS identifier, AggregateVersion version) {
        super(date, uuid, identifier, version);
    }

    @Override
    public String aggregateName() {
        return AggregateNames.INDIVIDUALS;
    }

    @Override
    public RequestsEventType eventType() {
        return RequestsEventType.INDIVIDUAL_UNREGISTERED;
    }
}
