package test.requests.service.impl.converters;

import org.springframework.stereotype.Component;
import test.common.domain.AggregateVersion;
import test.common.service.DomainEventConverter;
import test.requests.domain.ApplicationNumber;
import test.requests.domain.OperatorId;
import test.requests.domain.events.ApplicationRegisteredEvent;
import test.requests.domain.events.avro.ApplicationRegistered;

@Component
public class ApplicationRegisteredConverter implements DomainEventConverter<ApplicationRegisteredEvent, ApplicationRegistered> {

    @Override
    public ApplicationRegisteredEvent fromAvro(ApplicationRegistered object) {
        return new ApplicationRegisteredEvent(object.getDate(), object.getUuid(),
                new ApplicationNumber(object.getApplicationNumber()), new AggregateVersion(object.getVersion()),
                new OperatorId(object.getOperatorId()));
    }

    @Override
    public Class<ApplicationRegistered> recordType() {
        return ApplicationRegistered.class;
    }

    @Override
    public ApplicationRegistered toAvro(ApplicationRegisteredEvent object) {
        return ApplicationRegistered.newBuilder()
                .setDate(object.date().getTime())
                .setUuid(object.uuid().toString())
                .setVersion(object.version().version())
                .setApplicationNumber(object.aggregateIdentifier().number())
                .setOperatorId(object.operator().id())
                .build();
    }

    @Override
    public Class<ApplicationRegisteredEvent> objectType() {
        return ApplicationRegisteredEvent.class;
    }
}
