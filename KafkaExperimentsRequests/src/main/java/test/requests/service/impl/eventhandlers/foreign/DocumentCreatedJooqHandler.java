package test.requests.service.impl.eventhandlers.foreign;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import test.common.domain.PersistenceContextForeignEventHandler;
import test.common.service.JooqPersistenceContextHolder;
import test.requests.domain.events.foreign.DocumentCreatedEvent;

import static test.requests.persistence.db.Tables.APPLICATION_SECTIONS;

/**
 * @author s.smitienko
 */
@Component
@Slf4j
public class DocumentCreatedJooqHandler extends PersistenceContextForeignEventHandler<JooqPersistenceContextHolder, DocumentCreatedEvent> {

    public DocumentCreatedJooqHandler(JooqPersistenceContextHolder holder) {
        super(holder);
    }

    @Override
    @Transactional
    protected void handle(DocumentCreatedEvent event,
                          JooqPersistenceContextHolder holder) {

        DSLContext context = holder.get();

        Long sectionId = executeInsertSection(event, context);
        log.info("Created section [{0}] with ID [{1}]", event.name(),
                sectionId);
    }

    private Long executeInsertSection(DocumentCreatedEvent event,
                                      DSLContext context) {
        return context.insertInto(APPLICATION_SECTIONS,
                APPLICATION_SECTIONS.NAME,
                APPLICATION_SECTIONS.DESCRIPTION,
                APPLICATION_SECTIONS.DOCUMENT_ID)
                .values(event.name(), event.description(),
                        Long.parseLong(event.documentId()))
                .returning(APPLICATION_SECTIONS.ID)
                .fetchOne().getId();
    }

    @Override
    public Class<DocumentCreatedEvent> getEventClass() {
        return DocumentCreatedEvent.class;
    }
}
