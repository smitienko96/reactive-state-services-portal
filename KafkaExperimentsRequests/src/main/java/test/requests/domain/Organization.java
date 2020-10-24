package test.requests.domain;

import lombok.extern.slf4j.Slf4j;
import test.common.domain.*;
import test.requests.domain.events.OrganizationRegisteredEvent;

@Slf4j
@AggregateMetadata(name = AggregateNames.ORGANIZATIONS, mode = AggregateMode.EVENT_SOURCING)
public class Organization extends Applicant<INN> {

    private String shortName;
    private String longName;

    public Organization(EventPublisher publisher, INN identifier, AggregateVersion version, String shortName, String longName) {
        super(publisher, identifier, version, ApplicantType.LEGAL);
        this.shortName = shortName;
        this.longName = longName;
    }

    public String shortName() {
        return shortName;
    }

    public String longName() {
        return longName;
    }

    public INN inn() {
        return identifier();
    }


    @Override
    public OrganizationRegisteredEvent createInitialEvent() {
        return new OrganizationRegisteredEvent(shortName, longName);
    }
}
