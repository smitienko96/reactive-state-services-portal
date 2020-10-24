package test.requests.service.impl.eventhandlers;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import test.common.domain.PersistenceContextDomainEventHandler;
import test.common.service.JooqPersistenceContextHolder;
import test.requests.domain.events.IndividualRegisteredEvent;

import static test.requests.persistence.db.Tables.ADDRESSES;
import static test.requests.persistence.db.Tables.INDIVIDUALS;

@Component
@Slf4j
public class IndividualRegisteredJooqHandler extends PersistenceContextDomainEventHandler<JooqPersistenceContextHolder, IndividualRegisteredEvent> {

    private static final String ALPHA_NUMERIC_STRING = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ0123456789";

    public IndividualRegisteredJooqHandler(JooqPersistenceContextHolder holder) {
        super(holder);
    }

    /**
     * Генерирует случайную стоку, состоящую из
     * цифр и букв русского алфавита
     *
     * @param count
     * @return
     */
    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }


    @Override
    @Transactional
    protected void handle(IndividualRegisteredEvent event, JooqPersistenceContextHolder context) {
        DSLContext dsl = context.get();
        Long livingAddressId = createAddress(event, dsl);
        Long registrationAddressId = createAddress(event, dsl);

        int count = dsl.insertInto(INDIVIDUALS, INDIVIDUALS.IDENTIFIER,
                INDIVIDUALS.FIRST_NAME, INDIVIDUALS.LAST_NAME, INDIVIDUALS.PATRONYMIC,
                INDIVIDUALS.LIVING_ADDRESS_ID, INDIVIDUALS.REGISTRATION_ADDRESS_ID)
                .values(event.aggregateIdentifier().snilsValue(), event.firstName(), event.lastName(),
                        event.patronymic(), livingAddressId, registrationAddressId).execute();

        log.info("{} inserted into INDIVIDUALS table", count);
    }

    @Override
    public Class<IndividualRegisteredEvent> getEventClass() {
        return IndividualRegisteredEvent.class;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected Long createAddress(IndividualRegisteredEvent event, DSLContext context) {
        return context.insertInto(ADDRESSES, ADDRESSES.REGION, ADDRESSES.CITY, ADDRESSES.STREET)
                .values("Сланцевский район", "Сланцы", randomAlphaNumeric(10))
                .returning(ADDRESSES.ID).fetchOne().getId();
    }

}
