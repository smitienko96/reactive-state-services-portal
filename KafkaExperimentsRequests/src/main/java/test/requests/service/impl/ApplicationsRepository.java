package test.requests.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import test.common.domain.*;
import test.requests.domain.Application;
import test.requests.domain.ApplicationBuilder;
import test.requests.domain.ApplicationNumber;
import test.requests.domain.IApplicationsRepository;
import test.requests.domain.events.ApplicationArchivedEvent;
import test.requests.domain.events.ApplicationDraftHandedOverEvent;
import test.requests.service.IOperatorService;

/**
 * @author s.smitienko
 */
@Component
public class ApplicationsRepository extends
        ProjectingAggregateRepository<Application, ApplicationNumber, ApplicationDraftHandedOverEvent>
        implements IApplicationsRepository {

    @Autowired
    private IOperatorService operatorService;

    @Override
    protected Application createInitialState(ApplicationDraftHandedOverEvent initial, AggregateVersion version, EventPublisher publisher) {

        BuilderResult<Application> result = ApplicationBuilder.restore(publisher, version)
                .withNumber(initial.aggregateIdentifier())
                .withApplicationType(initial.applicationType())
                .withApplicant(initial.applicantId())
                .withOperator(initial.operatorId())
                .withApplicantDocumentTypes(initial.documentTypes())
                .withApplicantDocumentField(initial.documentFields())
                .build();

        return result.getAggregate();
    }


    @Override
    protected ApplicationArchivedEvent createAggregateDeletedEvent(Application aggregate) {
        return new ApplicationArchivedEvent(aggregate, operatorService.getCurrentOperator());
    }

    @Override
    protected String aggregateName() {
        return AggregateNames.APPLICATIONS;
    }



}
