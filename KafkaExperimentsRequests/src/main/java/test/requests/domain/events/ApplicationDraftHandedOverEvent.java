package test.requests.domain.events;

import lombok.NoArgsConstructor;
import test.common.domain.AggregateCreatedEvent;
import test.common.domain.AggregateNames;
import test.requests.domain.*;

import java.util.List;

@NoArgsConstructor
public class ApplicationDraftHandedOverEvent extends AggregateCreatedEvent<ApplicationNumber, RequestsEventType> {

    private ApplicationType applicationType;
    private ApplicantId applicantId;
    private OperatorId operatorId;

    private List<ApplicantDocumentType> documentTypes;
    private List<ApplicantDocumentField> documentFields;

    public ApplicationDraftHandedOverEvent(long date, String uuid, ApplicationNumber identifier,
                                           ApplicationType applicationType, ApplicantId applicantId,
                                           OperatorId operatorId, List<ApplicantDocumentType> documentTypes,
                                           List<ApplicantDocumentField> documentFields) {
        super(date, uuid, identifier);
        this.applicationType = applicationType;
        this.applicantId = applicantId;
        this.operatorId = operatorId;
        this.documentTypes = documentTypes;
        this.documentFields = documentFields;
    }

    public ApplicationDraftHandedOverEvent(ApplicationType applicationType, ApplicantId applicantId,
                                           OperatorId operatorId, List<ApplicantDocumentType> documentTypes, List<ApplicantDocumentField> documentFields) {
        this.applicationType = applicationType;
        this.applicantId = applicantId;
        this.operatorId = operatorId;
        this.documentTypes = documentTypes;
        this.documentFields = documentFields;
    }

    public ApplicationType applicationType() { return applicationType; }

    public ApplicantId applicantId() { return applicantId; }

    public OperatorId operatorId() { return operatorId; }

    public List<ApplicantDocumentType> documentTypes() {
        return documentTypes;
    }

    public List<ApplicantDocumentField> documentFields() { return documentFields; }

    @Override
    public String aggregateName() {
        return AggregateNames.APPLICATIONS;
    }

    @Override
    public RequestsEventType eventType() {
        return RequestsEventType.DRAFT_HANDED_OVER;
    }
}
