package test.requests.domain;

import test.common.domain.AggregateVersion;
import test.common.domain.ApplicantType;
import test.common.domain.DomainAggregate;
import test.common.domain.EventPublisher;

/**
 *
 * @param <I>
 */
public abstract class Applicant<I extends ApplicantId> extends DomainAggregate<I> {

    private ApplicantType applicantType;

    protected Applicant(EventPublisher publisher, I identifier, ApplicantType applicantType) {
        super(publisher, identifier);
        this.applicantType = applicantType;
    }

    protected Applicant(EventPublisher publisher, I identifier, AggregateVersion version, ApplicantType applicantType) {
        super(publisher, identifier, version);
        this.applicantType = applicantType;
    }

    /**
     *
     * @return
     */
    public ApplicantType applicantType() { return applicantType; }

}
