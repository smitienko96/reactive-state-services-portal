package test.regulations.domain.events;

import lombok.NoArgsConstructor;
import test.common.domain.AggregateCreatedEvent;
import test.common.domain.AggregateNames;
import test.common.domain.ApplicantType;
import test.regulations.domain.Regulation;
import test.regulations.domain.RegulationIdentifier;
import test.regulations.domain.ServiceId;

import java.util.List;

@NoArgsConstructor
public class RegulationRegisteredEvent extends AggregateCreatedEvent<RegulationIdentifier, RegulationsEventType> {

    private ServiceId serviceId;
    private List<ApplicantType> allowedApplicantTypes;
    private boolean questionaryRequired;
    private Regulation.Status status;

    public RegulationRegisteredEvent(ServiceId serviceId, List<ApplicantType> allowedApplicantTypes, boolean questionaryRequired, Regulation.Status status) {
        this.serviceId = serviceId;
        this.allowedApplicantTypes = allowedApplicantTypes;
        this.questionaryRequired = questionaryRequired;
        this.status = status;
    }

    public ServiceId serviceId() {
        return serviceId;
    }

    @Override
    public String aggregateName() {
        return AggregateNames.REGULATIONS;
    }

    public List<ApplicantType> allowedApplicantTypes() {
        return allowedApplicantTypes;
    }

    public boolean isQuestionaryRequired() {
        return questionaryRequired;
    }

    public Regulation.Status status() {
        return status;
    }

    @Override
    public RegulationsEventType eventType() {
        return RegulationsEventType.REGULATION_REGISTERED;
    }
}
