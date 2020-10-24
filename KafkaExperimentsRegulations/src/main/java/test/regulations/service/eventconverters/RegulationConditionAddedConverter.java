package test.regulations.service.eventconverters;

import org.springframework.stereotype.Component;
import test.common.domain.AggregateVersion;
import test.common.service.DomainEventConverter;
import test.regulations.domain.ConditionCode;
import test.regulations.domain.ConditionDefinition;
import test.regulations.domain.RegulationIdentifier;
import test.regulations.domain.events.RegulationConditionAddedEvent;
import test.regulations.domain.events.avro.RegulationConditionAdded;

import java.util.Date;

@Component
public class RegulationConditionAddedConverter implements DomainEventConverter<RegulationConditionAddedEvent, RegulationConditionAdded> {


    @Override
    public RegulationConditionAddedEvent fromAvro(RegulationConditionAdded object) {
        return new RegulationConditionAddedEvent(object.getDate(), object.getUuid(),
                new RegulationIdentifier(object.getRegulationNumber(), new Date(object.getRegulationDate())),
                new AggregateVersion(object.getVersion()),
                new ConditionCode(object.getConditionCode()),
                new ConditionDefinition(object.getConditionName(), object.getConditionDescription()),
                object.getConditionOrder());
    }

    @Override
    public Class<RegulationConditionAdded> recordType() {
        return RegulationConditionAdded.class;
    }

    @Override
    public RegulationConditionAdded toAvro(RegulationConditionAddedEvent object) {
        return RegulationConditionAdded.newBuilder()
                .setDate(object.date().getTime())
                .setUuid(object.uuid().toString())
                .setVersion(object.version().version())
                .setRegulationNumber(object.aggregateIdentifier().number())
                .setRegulationDate(object.aggregateIdentifier().date().getTime())
                .setConditionCode(object.conditionCode().value())
                .setConditionName(object.definition().name())
                .setConditionDescription(object.definition().description())
                .setConditionOrder(object.order())
                .build();
    }

    @Override
    public Class<RegulationConditionAddedEvent> objectType() {
        return RegulationConditionAddedEvent.class;
    }
}
