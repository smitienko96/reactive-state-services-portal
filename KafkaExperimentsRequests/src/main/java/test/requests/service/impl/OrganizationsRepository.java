package test.requests.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import test.common.domain.AggregateNames;
import test.common.domain.AggregateVersion;
import test.common.domain.EventPublisher;
import test.common.domain.ProjectingAggregateRepository;
import test.requests.domain.INN;
import test.requests.domain.IOrganizationsRepository;
import test.requests.domain.Organization;
import test.requests.domain.OrganizationBuilder;
import test.requests.domain.events.OrganizationRegisteredEvent;
import test.requests.domain.events.OrganizationUnregisteredEvent;

@Component
@Slf4j
public class OrganizationsRepository extends ProjectingAggregateRepository<Organization, INN, OrganizationRegisteredEvent> implements IOrganizationsRepository {

    @Override
    public Mono<Organization> get(INN identifier) {
        return Mono.empty();
    }

    @Override
    public Mono<Boolean> doSave(Organization aggregate) {
        return null;
    }

    @Override
    protected String aggregateName() {
        return AggregateNames.ORGANIZATIONS;
    }

    @Override
    protected Organization createInitialState(OrganizationRegisteredEvent initialEvent, AggregateVersion lastVersion, EventPublisher publisher) {
        return OrganizationBuilder.restore(publisher, lastVersion)
                .withINN(initialEvent.aggregateIdentifier())
                .withShortName(initialEvent.shortName())
                .withLongName(initialEvent.longName())
                .build().getAggregate();
    }

    @Override
    protected OrganizationUnregisteredEvent createAggregateDeletedEvent(Organization aggregate) {
        return new OrganizationUnregisteredEvent(aggregate);
    }

}
