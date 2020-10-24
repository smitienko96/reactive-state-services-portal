package test.requests.domain;

import test.common.domain.AggregateBuilder;
import test.common.domain.AggregateVersion;
import test.common.domain.EventPublisher;

import java.util.List;

public class ApplicationBuilder extends AggregateBuilder<Application> {

    private ApplicationNumber number;
    private ApplicationType applicationType;

    private ApplicantId applicant;
    private OperatorId operator;
    private List<ApplicantDocumentType> applicantDocumentTypes;

    private List<ApplicantDocumentField> applicantDocumentField;

    protected ApplicationBuilder(EventPublisher publisher) {
        super(publisher);
    }

    protected ApplicationBuilder(EventPublisher publisher, AggregateVersion version) {
        super(publisher, version);
    }

    public static ApplicationBuilder fresh(EventPublisher publisher) {
        return new ApplicationBuilder(publisher);
    }

    public static ApplicationBuilder restore(EventPublisher publisher, AggregateVersion version) {
        return new ApplicationBuilder(publisher, version);
    }

    public ApplicationBuilder withNumber(ApplicationNumber number) {
        this.number = number;

        return this;
    }

    public ApplicationBuilder withApplicationType(ApplicationType applicationType) {
        this.applicationType = applicationType;

        return this;
    }

    public ApplicationBuilder withApplicant(ApplicantId applicant) {
        this.applicant = applicant;

        return this;
    }

    public ApplicationBuilder withOperator(OperatorId operator) {
        this.operator = operator;

        return this;
    }

    public ApplicationBuilder withApplicantDocumentTypes(List<ApplicantDocumentType> applicantDocumentTypes) {
        this.applicantDocumentTypes = applicantDocumentTypes;

        return this;
    }

    public ApplicationBuilder withApplicantDocumentField(List<ApplicantDocumentField> applicantDocumentField) {
        this.applicantDocumentField = applicantDocumentField;

        return this;
    }

    @Override
    protected Application assemblyFresh() {
        return assembly();
    }

    @Override
    protected Application assemblyRestore() {
        return assembly();
    }

    private Application assembly() {
        return new Application(publisher, number, version, applicationType, applicant,
                operator, applicantDocumentTypes, applicantDocumentField);
    }
}
