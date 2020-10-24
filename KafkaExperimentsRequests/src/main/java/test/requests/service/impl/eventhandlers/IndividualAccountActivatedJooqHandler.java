package test.requests.service.impl.eventhandlers;

import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import test.common.domain.PersistenceContextDomainEventHandler;
import test.common.service.JooqPersistenceContextHolder;
import test.requests.domain.events.IndividualAccountActivatedEvent;

import static test.requests.persistence.db.Tables.INDIVIDUALS;

@Component
public class IndividualAccountActivatedJooqHandler extends PersistenceContextDomainEventHandler<JooqPersistenceContextHolder, IndividualAccountActivatedEvent> {


    public IndividualAccountActivatedJooqHandler(JooqPersistenceContextHolder holder) {
        super(holder);
    }

    @Override
    @Transactional
    protected void handle(IndividualAccountActivatedEvent event, JooqPersistenceContextHolder holder) {
        DSLContext context = holder.get();

        context.update(INDIVIDUALS)
                .set(INDIVIDUALS.ACTIVE, true)
                .where(INDIVIDUALS.IDENTIFIER.eq(event.aggregateIdentifier().snilsValue()))
                .execute();
    }

    @Override
    public Class<IndividualAccountActivatedEvent> getEventClass() {
        return IndividualAccountActivatedEvent.class;
    }

}
