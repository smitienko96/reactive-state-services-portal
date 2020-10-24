package test.requests.service.impl.eventhandlers;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import test.common.domain.PersistenceContextDomainEventHandler;
import test.common.service.JooqPersistenceContextHolder;
import test.requests.domain.events.ApplicationArchivedEvent;

import static test.requests.persistence.db.Tables.APPLICATIONS_;

@Component
@Slf4j
public class ApplicationArchivedJooqHandler extends PersistenceContextDomainEventHandler<JooqPersistenceContextHolder, ApplicationArchivedEvent> {

    public ApplicationArchivedJooqHandler(JooqPersistenceContextHolder holder) {
        super(holder);
    }

    @Override
    @Transactional
    protected void handle(ApplicationArchivedEvent event, JooqPersistenceContextHolder holder) {
        DSLContext context = holder.get();

        int count = context.delete(APPLICATIONS_)
                .where(APPLICATIONS_.NUMBER.eq(event.getIdentifier().number()))
                .execute();

        log.info("{} applications deleted for ApplicationNumber [{}]", count, event.getIdentifier().number());
    }

    @Override
    public Class<ApplicationArchivedEvent> getEventClass() {
        return ApplicationArchivedEvent.class;
    }


}
