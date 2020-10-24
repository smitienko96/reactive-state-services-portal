package test.requests.service.impl.converters;

import org.springframework.stereotype.Component;
import test.common.domain.AggregateVersion;
import test.common.service.DomainEventConverter;
import test.requests.domain.SNILS;
import test.requests.domain.events.IndividualAccountActivatedEvent;
import test.requests.domain.events.avro.IndividualAccountActivated;

@Component
public class IndividualAccountActivatedConverter implements DomainEventConverter<IndividualAccountActivatedEvent, IndividualAccountActivated> {

    @Override
    public IndividualAccountActivatedEvent fromAvro(IndividualAccountActivated object) {
        return new IndividualAccountActivatedEvent(object.getDate(), object.getUuid(),
                new SNILS(object.getSnils()), new AggregateVersion(object.getVersion()));
    }

    @Override
    public Class<IndividualAccountActivated> recordType() {
        return IndividualAccountActivated.class;
    }

    @Override
    public IndividualAccountActivated toAvro(IndividualAccountActivatedEvent object) {
        return IndividualAccountActivated.newBuilder()
                .setDate(object.date().getTime())
                .setUuid(object.uuid().toString())
                .setSnils(object.aggregateIdentifier().snilsValue())
                .setVersion(object.version().version())
                .build();
    }

    @Override
    public Class<IndividualAccountActivatedEvent> objectType() {
        return IndividualAccountActivatedEvent.class;
    }
}
