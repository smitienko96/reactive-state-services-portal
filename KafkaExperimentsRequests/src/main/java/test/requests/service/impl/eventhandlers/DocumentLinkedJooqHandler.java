package test.requests.service.impl.eventhandlers;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import test.common.domain.PersistenceContextDomainEventHandler;
import test.common.service.JooqPersistenceContextHolder;
import test.requests.domain.events.DocumentLinkedEvent;

import static test.requests.persistence.db.Tables.APPLICATIONS_;
import static test.requests.persistence.db.Tables.APPLICATION_SECTIONS;

/**
 * @author s.smitienko
 */
@Component
@Slf4j
public class DocumentLinkedJooqHandler extends PersistenceContextDomainEventHandler<JooqPersistenceContextHolder, DocumentLinkedEvent> {

    public DocumentLinkedJooqHandler(JooqPersistenceContextHolder holder) {
        super(holder);
    }

    @Override
    @Transactional
    protected void handle(DocumentLinkedEvent event, JooqPersistenceContextHolder context) {
        DSLContext dsl = context.get();
        linkDocumentToSection(event, dsl);
    }

    private void linkDocumentToSection(DocumentLinkedEvent event,
                                       DSLContext context) {
        Long documentId = Long.valueOf(event.documentId().id());
        String applicationNumber = event.aggregateIdentifier().number();
        String documentCode = event.documentType().typeCode().code();
        Long applicationId = context.select(APPLICATIONS_.ID)
                .from(APPLICATIONS_)
                .where(APPLICATIONS_.NUMBER.eq(applicationNumber))
                .fetchOne(APPLICATIONS_.ID);


        context.update(APPLICATION_SECTIONS)
                .set(APPLICATION_SECTIONS.APPLICATION_ID, applicationId)
                .set(APPLICATION_SECTIONS.DOCUMENT_CODE, documentCode)
                .set(APPLICATION_SECTIONS.DOCUMENT_DESIGNATION, event.designation())
                .where(APPLICATION_SECTIONS.DOCUMENT_ID.eq(documentId))
                .execute();
    }


    @Override
    public Class<DocumentLinkedEvent> getEventClass() {
        return DocumentLinkedEvent.class;
    }
}
