package test.requests.service.impl.converters;

import org.springframework.stereotype.Component;
import test.common.domain.AggregateVersion;
import test.common.service.DomainEventConverter;
import test.requests.domain.ApplicationNumber;
import test.requests.domain.Status;
import test.requests.domain.events.ApplicationStatusChangedEvent;
import test.requests.domain.events.avro.ApplicationStatusChanged;

/**
 * @author s.smitienko
 */
@Component
public class ApplicationStatusChangedConverter implements DomainEventConverter<ApplicationStatusChangedEvent, ApplicationStatusChanged> {

    @Override
    public ApplicationStatusChanged toAvro(ApplicationStatusChangedEvent domainEvent) {
        return ApplicationStatusChanged.newBuilder().setUuid(domainEvent.uuid().toString())
                .setDate(domainEvent.date().getTime())
                .setVersion(domainEvent.version().version())
                .setApplicationNumber(domainEvent.aggregateIdentifier().number())
                .setNewStatus(domainEvent.newStatus().name())
                .build();
    }

    @Override
    public ApplicationStatusChangedEvent fromAvro(ApplicationStatusChanged record) {
        return new ApplicationStatusChangedEvent(record.getDate(),
                record.getUuid(),
                new ApplicationNumber(record.getApplicationNumber()),
                new AggregateVersion(record.getVersion()),
                Status.valueOf(record.getNewStatus()));
    }

    @Override
    public Class<ApplicationStatusChangedEvent> objectType() {
        return ApplicationStatusChangedEvent.class;
    }

    @Override
    public Class<ApplicationStatusChanged> recordType() {
        return ApplicationStatusChanged.class;
    }
}
