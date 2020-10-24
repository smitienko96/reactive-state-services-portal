package test.regulations.service.eventconverters;

import org.springframework.stereotype.Component;
import test.common.domain.ApplicantType;
import test.common.service.DomainEventConverter;
import test.regulations.domain.Regulation;
import test.regulations.domain.ServiceId;
import test.regulations.domain.events.RegulationRegisteredEvent;
import test.regulations.domain.events.avro.RegulationRegistered;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RegulationRegisteredConverter implements DomainEventConverter<RegulationRegisteredEvent, RegulationRegistered> {

    @Override
    public RegulationRegisteredEvent fromAvro(RegulationRegistered object) {
        List<ApplicantType> applicantTypes = object.getAllowedApplicantTypes()
                .stream().map(ApplicantType::valueOf)
                .collect(Collectors.toList());
        Regulation.Status status = Regulation.Status.valueOf(object.getStatus());

        return new RegulationRegisteredEvent(new ServiceId(object.getServiceId()),
                applicantTypes, object.getQuestionaryRequired(), status);
    }

    @Override
    public Class<RegulationRegistered> recordType() {
        return RegulationRegistered.class;
    }

    @Override
    public RegulationRegistered toAvro(RegulationRegisteredEvent object) {
        List<String> allowedApplicantTypes = object.allowedApplicantTypes().stream()
                .map(ApplicantType::name).collect(Collectors.toList());
        return RegulationRegistered.newBuilder()
                .setUuid(object.uuid().toString())
                .setDate(object.date().getTime())
                .setRegulationNumber(object.aggregateIdentifier().number())
                .setRegulationDate(object.aggregateIdentifier().date().getTime())
                .setServiceId(object.serviceId().id())
                .setQuestionaryRequired(object.isQuestionaryRequired())
                .setAllowedApplicantTypes(allowedApplicantTypes)
                .setStatus(object.status().name())
                .build();
    }

    @Override
    public Class<RegulationRegisteredEvent> objectType() {
        return RegulationRegisteredEvent.class;
    }
}
