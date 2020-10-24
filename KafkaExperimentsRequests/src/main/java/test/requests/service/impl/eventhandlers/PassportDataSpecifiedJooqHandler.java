package test.requests.service.impl.eventhandlers;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import test.common.domain.PersistenceContextDomainEventHandler;
import test.common.service.JooqPersistenceContextHolder;
import test.requests.domain.events.PassportDataSpecifiedEvent;

import java.sql.Date;

import static test.requests.persistence.db.Tables.INDIVIDUALS;

@Component
@Slf4j
public class PassportDataSpecifiedJooqHandler extends
        PersistenceContextDomainEventHandler<JooqPersistenceContextHolder, PassportDataSpecifiedEvent> {

    public PassportDataSpecifiedJooqHandler(JooqPersistenceContextHolder holder) {
        super(holder);
    }

    @Override
    @Transactional
    protected void handle(PassportDataSpecifiedEvent event, JooqPersistenceContextHolder holder) {
        DSLContext context = holder.get();

        context.update(INDIVIDUALS)
                .set(INDIVIDUALS.PASSPORT_SERIES, event.passportData().series())
                .set(INDIVIDUALS.PASSPORT_NO, event.passportData().number())
                .set(INDIVIDUALS.PASSPORT_ISSUE_DATE, new Date(event.passportData().issueDate().getTime()))
                .set(INDIVIDUALS.PASSPORT_ISSUE_AUTHORITY, event.passportData().issuingAuthority())
                .set(INDIVIDUALS.REG_DATE, new Date(event.passportData().issueDate().getTime()))
                .where(INDIVIDUALS.IDENTIFIER.eq(event.aggregateIdentifier().snilsValue()))
                .execute();
    }

    @Override
    public Class<PassportDataSpecifiedEvent> getEventClass() {
        return PassportDataSpecifiedEvent.class;
    }

}
