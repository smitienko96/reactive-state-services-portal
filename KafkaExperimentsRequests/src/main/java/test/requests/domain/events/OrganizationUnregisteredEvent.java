package test.requests.domain.events;

import test.common.domain.AggregateDeletedEvent;
import test.common.domain.AggregateNames;
import test.common.domain.AggregateVersion;
import test.requests.domain.RequestsEventType;
import test.requests.domain.INN;
import test.requests.domain.Organization;

public class OrganizationUnregisteredEvent extends AggregateDeletedEvent<Organization, INN, RequestsEventType> {


    public OrganizationUnregisteredEvent(Organization aggregate) {
        super(aggregate);
    }

    public OrganizationUnregisteredEvent(long date, String uuid, INN identifier, AggregateVersion version) {
        super(date, uuid, identifier, version);
    }

    @Override
    public String aggregateName() {
        return AggregateNames.ORGANIZATIONS;
    }

    @Override
    public RequestsEventType eventType() {
        return RequestsEventType.ORGANIZATION_UNREGISTERED;
    }
}
