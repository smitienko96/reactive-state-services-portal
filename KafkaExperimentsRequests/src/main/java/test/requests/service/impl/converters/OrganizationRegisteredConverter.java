package test.requests.service.impl.converters;

import org.springframework.stereotype.Component;
import test.common.service.DomainEventConverter;
import test.requests.domain.INN;
import test.requests.domain.events.OrganizationRegisteredEvent;
import test.requests.domain.events.avro.OrganizationRegistered;

@Component
public class OrganizationRegisteredConverter implements DomainEventConverter<OrganizationRegisteredEvent, OrganizationRegistered> {

    @Override
    public OrganizationRegisteredEvent fromAvro(OrganizationRegistered object) {
        return new OrganizationRegisteredEvent(object.getDate(), object.getUuid(),
                new INN(object.getInn()), object.getShortName(), object.getLongName());
    }

    @Override
    public Class<OrganizationRegistered> recordType() {
        return OrganizationRegistered.class;
    }

    @Override
    public OrganizationRegistered toAvro(OrganizationRegisteredEvent object) {
        return OrganizationRegistered.newBuilder()
                .setDate(object.date().getTime())
                .setUuid(object.uuid().toString())
                .setInn(object.aggregateIdentifier().innValue())
                .setLongName(object.longName())
                .setShortName(object.shortName())
                .build();
    }

    @Override
    public Class<OrganizationRegisteredEvent> objectType() {
        return OrganizationRegisteredEvent.class;
    }
}
