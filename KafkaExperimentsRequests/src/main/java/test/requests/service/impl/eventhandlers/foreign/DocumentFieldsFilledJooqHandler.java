package test.requests.service.impl.eventhandlers.foreign;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep5;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import test.common.domain.PersistenceContextForeignEventHandler;
import test.common.service.JooqPersistenceContextHolder;
import test.requests.domain.events.foreign.DocumentFieldsFilledEvent;

import java.util.Map;

import static test.requests.persistence.db.Tables.APPLICATION_DOCUMENT_FIELDS;
import static test.requests.persistence.db.Tables.APPLICATION_SECTIONS;

/**
 * @author s.smitienko
 */
@Component
@Slf4j
public class DocumentFieldsFilledJooqHandler extends PersistenceContextForeignEventHandler<JooqPersistenceContextHolder, DocumentFieldsFilledEvent> {

    public DocumentFieldsFilledJooqHandler(JooqPersistenceContextHolder holder) {
        super(holder);
    }

    @Override
    @Transactional
    protected void handle(DocumentFieldsFilledEvent event, JooqPersistenceContextHolder holder) {
        DSLContext dsl = holder.get();

        Long sectionId = getSectionId(event.documentId(), dsl);
        int count = executeInsertFields(event, sectionId, dsl);
        log.info("Inserted {0} fields into section [{1}]", count, sectionId);

    }

    private Long getSectionId(String documentId, DSLContext context) {
        Long id = Long.valueOf(documentId);
        return context.select(APPLICATION_SECTIONS.ID).from(APPLICATION_SECTIONS)
                .where(APPLICATION_SECTIONS.DOCUMENT_ID.eq(id))
                .fetchAny(APPLICATION_SECTIONS.ID);
    }

    private int executeInsertFields(DocumentFieldsFilledEvent event, Long sectionId,
                                    DSLContext context) {
        Map<DocumentFieldsFilledEvent.DocumentField, String> fieldsMap =
                event.documentFields();
        if (fieldsMap == null || fieldsMap.isEmpty()) {
            return 0;
        }
        InsertValuesStep5 insertValues =
                context.insertInto(APPLICATION_DOCUMENT_FIELDS,
                        APPLICATION_DOCUMENT_FIELDS.SECTION_ID,
                        APPLICATION_DOCUMENT_FIELDS.NAME,
                        APPLICATION_DOCUMENT_FIELDS.DESCRIPTION,
                        APPLICATION_DOCUMENT_FIELDS.FIELD_TYPE,
                        APPLICATION_DOCUMENT_FIELDS.VALUE);

        fieldsMap.forEach((field, value) -> {
            insertValues.values(sectionId, field.getName(),
                    field.getDescription(), field.getType(), value);
        });

        return insertValues.execute();
    }

    @Override
    public Class<DocumentFieldsFilledEvent> getEventClass() {
        return DocumentFieldsFilledEvent.class;
    }
}
