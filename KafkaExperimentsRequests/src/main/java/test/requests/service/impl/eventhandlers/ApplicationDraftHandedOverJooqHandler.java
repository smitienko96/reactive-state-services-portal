package test.requests.service.impl.eventhandlers;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import test.common.domain.PersistenceContextDomainEventHandler;
import test.common.service.JooqPersistenceContextHolder;
import test.requests.domain.Status;
import test.requests.domain.events.ApplicationDraftHandedOverEvent;

import static test.requests.persistence.db.Tables.APPLICATIONS_;
import static test.requests.persistence.db.Tables.INDIVIDUALS;

@Slf4j
@Component
public class ApplicationDraftHandedOverJooqHandler extends PersistenceContextDomainEventHandler<JooqPersistenceContextHolder, ApplicationDraftHandedOverEvent> {

    public ApplicationDraftHandedOverJooqHandler(JooqPersistenceContextHolder holder) {
        super(holder);
    }

    @Override
    protected void handle(ApplicationDraftHandedOverEvent event, JooqPersistenceContextHolder holder) {
        DSLContext dsl = holder.get();
        Long applicantId = dsl.select(INDIVIDUALS.ID).from(INDIVIDUALS)
                .where(INDIVIDUALS.IDENTIFIER.eq(event.applicantId().id())).fetchSingle(INDIVIDUALS.ID);
        Long identifier = dsl.insertInto(APPLICATIONS_, APPLICATIONS_.NUMBER, APPLICATIONS_.STATUS, APPLICATIONS_.APPLICATION_TYPE, APPLICATIONS_.APPLICANT_ID, APPLICATIONS_.CREATOR_ID)
                .values(event.aggregateIdentifier().number(), Status.DRAFT.name(), event.applicationType().typeCode(), applicantId, event.operatorId().id())
                .returning(APPLICATIONS_.ID).fetchOne().getId();

        if (identifier != null) {
            log.info("Application record inserted for application number {}", event.aggregateIdentifier().number());
        } else {
            log.warn("No application record inserted for application number {}", event.aggregateIdentifier().number());
            return;
        }
    }

    @Override
    public Class<ApplicationDraftHandedOverEvent> getEventClass() {
        return ApplicationDraftHandedOverEvent.class;
    }
}
