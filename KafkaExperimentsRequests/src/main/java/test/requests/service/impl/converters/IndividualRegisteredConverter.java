package test.requests.service.impl.converters;

import org.springframework.stereotype.Component;
import test.common.service.DomainEventConverter;
import test.requests.domain.SNILS;
import test.requests.domain.events.IndividualRegisteredEvent;
import test.requests.domain.events.avro.IndividualRegistered;

@Component
public class IndividualRegisteredConverter implements DomainEventConverter<IndividualRegisteredEvent, IndividualRegistered> {

    @Override
    public IndividualRegisteredEvent fromAvro(IndividualRegistered object) {
        return new IndividualRegisteredEvent(object.getDate(), object.getUuid(),
                new SNILS(object.getSnils()), object.getFirstName(),
                object.getLastName(), object.getPatronymic());
    }

    @Override
    public Class<IndividualRegistered> recordType() {
        return IndividualRegistered.class;
    }

    @Override
    public IndividualRegistered toAvro(IndividualRegisteredEvent object) {
        return IndividualRegistered.newBuilder()
                .setSnils(object.aggregateIdentifier().snilsValue())
                .setUuid(object.uuid().toString())
                .setDate(object.date().getTime())
                .setFirstName(object.firstName())
                .setLastName(object.lastName())
                .setPatronymic(object.patronymic())
                .build();
    }

    @Override
    public Class<IndividualRegisteredEvent> objectType() {
        return IndividualRegisteredEvent.class;
    }
}
