package test.requests.domain.events;

import lombok.NoArgsConstructor;
import test.common.domain.AggregateNames;
import test.common.domain.AggregateVersion;
import test.common.domain.DomainEvent;
import test.requests.domain.ApplicationNumber;
import test.requests.domain.OperatorId;
import test.requests.domain.RequestsEventType;

@NoArgsConstructor
public class ApplicationRegisteredEvent extends DomainEvent<ApplicationNumber, RequestsEventType> {

    private OperatorId operatorId;

    public ApplicationRegisteredEvent(OperatorId operatorId) {
        this.operatorId = operatorId;
    }

    public ApplicationRegisteredEvent(long date, String uuid, ApplicationNumber identifier, AggregateVersion version, OperatorId operatorId) {
        super(date, uuid, identifier, version);
        this.operatorId = operatorId;
    }

    public OperatorId operator() {
        return operatorId;
    }


    @Override
    public String aggregateName() {
        return AggregateNames.APPLICATIONS;
    }

    @Override
    public RequestsEventType eventType() {
        return RequestsEventType.APPLICATION_REGISTERED;
    }

}
