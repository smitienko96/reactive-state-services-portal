package test.requests.domain.events;

import lombok.NoArgsConstructor;
import test.common.domain.AggregateCreatedEvent;
import test.common.domain.AggregateNames;
import test.requests.domain.RequestsEventType;
import test.requests.domain.INN;

@NoArgsConstructor
public class OrganizationRegisteredEvent extends AggregateCreatedEvent<INN, RequestsEventType> {

    private String shortName;
    private String longName;


    public OrganizationRegisteredEvent(String shortName, String longName) {
        this.shortName = shortName;
        this.longName = longName;
    }

    public OrganizationRegisteredEvent(long date, String uuid, INN identifier, String shortName, String longName) {
        super(date, uuid, identifier);
        this.shortName = shortName;
        this.longName = longName;
    }

    public String shortName() {
        return shortName;
    }

    public String longName() {
        return longName;
    }

    @Override
    public String aggregateName() {
        return AggregateNames.ORGANIZATIONS;
    }

    @Override
    public RequestsEventType eventType() {
        return RequestsEventType.ORGANIZATION_REGISTERED;
    }


}
