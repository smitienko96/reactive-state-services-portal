package test.requests.service.impl.converters;

import org.springframework.stereotype.Component;
import test.common.domain.AggregateVersion;
import test.common.service.DomainEventConverter;
import test.requests.domain.ApplicationNumber;
import test.requests.domain.OperatorId;
import test.requests.domain.events.ApplicationArchivedEvent;
import test.requests.domain.events.avro.ApplicationArchived;

@Component
public class ApplicationArchivedConverter implements DomainEventConverter<ApplicationArchivedEvent, ApplicationArchived> {

    @Override
    public ApplicationArchivedEvent fromAvro(ApplicationArchived object) {
        return new ApplicationArchivedEvent(object.getDate(), object.getUuid(),
                new ApplicationNumber(object.getApplicationNumber()),
                new AggregateVersion(object.getVersion()),
                new OperatorId(object.getOperatorId()));
    }

    @Override
    public Class<ApplicationArchived> recordType() {
        return ApplicationArchived.class;
    }

    @Override
    public ApplicationArchived toAvro(ApplicationArchivedEvent object) {
        return ApplicationArchived.newBuilder()
                .setDate(object.date().getTime())
                .setUuid(object.uuid().toString())
                .setApplicationNumber(object.aggregateIdentifier().number())
                .setVersion(object.version().version())
                .setOperatorId(object.operator().id())
                .build();
    }

    @Override
    public Class<ApplicationArchivedEvent> objectType() {
        return ApplicationArchivedEvent.class;
    }
}
