package test.common.service;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import test.common.domain.IPersistenceContextHolder;

/**
 * @author s.smitienko
 */
@Component
public class JooqPersistenceContextHolder implements IPersistenceContextHolder<DSLContext> {

    @Autowired
    private DSLContext dslContext;

    /**
     * Получает значение <поле>
     *
     * @return значение  <поле>
     */
    public DSLContext get() {
        return dslContext;
    }
}
