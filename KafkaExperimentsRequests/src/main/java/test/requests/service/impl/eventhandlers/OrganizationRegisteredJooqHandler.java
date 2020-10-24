package test.requests.service.impl.eventhandlers;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import test.common.domain.PersistenceContextDomainEventHandler;
import test.common.service.JooqPersistenceContextHolder;
import test.requests.domain.events.OrganizationRegisteredEvent;

import static test.requests.persistence.db.Tables.ADDRESSES;
import static test.requests.persistence.db.Tables.ORGANIZATIONS;

@Component
@Slf4j
public class OrganizationRegisteredJooqHandler extends PersistenceContextDomainEventHandler<JooqPersistenceContextHolder, OrganizationRegisteredEvent> {

    public OrganizationRegisteredJooqHandler(JooqPersistenceContextHolder holder) {
        super(holder);
    }

    @Override
    @Transactional
    protected void handle(OrganizationRegisteredEvent event, JooqPersistenceContextHolder holder) {
        DSLContext dsl = holder.get();

        Long actualAddressId = createAddress(event, dsl);
        Long legalAddressId = createAddress(event, dsl);
        int count = dsl.insertInto(ORGANIZATIONS, ORGANIZATIONS.IDENTIFIER, ORGANIZATIONS.INN,
                ORGANIZATIONS.ORG_NAME_FULL, ORGANIZATIONS.ORG_NAME_SHORT, ORGANIZATIONS.ACTUAL_ADDRESS_ID,
                ORGANIZATIONS.LEGAL_ADDRESS_ID)
                .values(event.aggregateIdentifier().innValue(), event.aggregateIdentifier().innValue(),
                        event.longName(), event.shortName(), actualAddressId, legalAddressId).execute();
        log.info("{} inserted into ORGANIZATIONS table", count);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected Long createAddress(OrganizationRegisteredEvent event, DSLContext context) {
        return context.insertInto(ADDRESSES, ADDRESSES.REGION, ADDRESSES.CITY, ADDRESSES.STREET)
                .values("Сланцевский район", "Сланцы", IndividualRegisteredJooqHandler.randomAlphaNumeric(10))
                .returning(ADDRESSES.ID).fetchOne().getId();
    }

    @Override
    public Class<OrganizationRegisteredEvent> getEventClass() {
        return OrganizationRegisteredEvent.class;
    }

}
