package test.regulations.service.eventconverters;

import org.springframework.stereotype.Component;
import test.common.domain.AggregateVersion;
import test.common.service.DomainEventConverter;
import test.regulations.domain.Regulation;
import test.regulations.domain.RegulationIdentifier;
import test.regulations.domain.events.RegulationStatusChangedEvent;
import test.regulations.domain.events.avro.RegulationStatusChanged;

import java.util.Date;

@Component
public class RegulationStatusChangedConverter implements DomainEventConverter<RegulationStatusChangedEvent, RegulationStatusChanged> {


    @Override
    public RegulationStatusChangedEvent fromAvro(RegulationStatusChanged object) {
        return new RegulationStatusChangedEvent(object.getDate(), object.getUuid(),
                new RegulationIdentifier(object.getRegulationNumber(), new Date(object.getRegulationDate())),
                new AggregateVersion(object.getVersion()), Regulation.Status.valueOf(object.getStatus()),
                new Date(object.getChangeDate()));
    }

    @Override
    public Class<RegulationStatusChanged> recordType() {
        return RegulationStatusChanged.class;
    }

    @Override
    public RegulationStatusChanged toAvro(RegulationStatusChangedEvent object) {
        return RegulationStatusChanged.newBuilder()
                .setDate(object.date().getTime())
                .setUuid(object.uuid().toString())
                .setVersion(object.version().version())
                .setRegulationNumber(object.aggregateIdentifier().number())
                .setRegulationDate(object.aggregateIdentifier().date().getTime())
                .setChangeDate(object.changeDate().getTime())
                .setStatus(object.status().name())
                .build();
    }

    @Override
    public Class<RegulationStatusChangedEvent> objectType() {
        return RegulationStatusChangedEvent.class;
    }
}
