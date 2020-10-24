package test.documents.service.eventconverters;

import org.springframework.stereotype.Component;
import test.common.domain.AggregateVersion;
import test.common.domain.DocumentTypeId;
import test.common.service.DomainEventConverter;
import test.documents.domain.events.DocumentFieldOptionAddedEvent;
import test.documents.domain.events.avro.DocumentFieldOptionAdded;

/**
 * @author s.smitienko
 */
@Component
public class DocumentFieldOptionAddedConverter implements DomainEventConverter<DocumentFieldOptionAddedEvent, DocumentFieldOptionAdded> {

    @Override
    public DocumentFieldOptionAdded toAvro(DocumentFieldOptionAddedEvent domainEvent) {
        return DocumentFieldOptionAdded.newBuilder()
                .setUuid(domainEvent.uuid().toString())
                .setDate(domainEvent.date().getTime())
                .setVersion(domainEvent.version().version())
                .setDocumentTypeId(domainEvent.aggregateIdentifier().id())
                .setFieldCode(domainEvent.fieldCode())
                .setOrder(domainEvent.order())
                .setDescription(domainEvent.description())
                .setValue(domainEvent.value())
                .build();
    }

    @Override
    public DocumentFieldOptionAddedEvent fromAvro(DocumentFieldOptionAdded record) {
        return new DocumentFieldOptionAddedEvent(record.getDate(),
                record.getUuid(),
                new DocumentTypeId(record.getDocumentTypeId()),
                new AggregateVersion(record.getVersion()),
                record.getFieldCode(), record.getOrder(), record.getValue(),
                record.getDescription());
    }

    @Override
    public Class<DocumentFieldOptionAddedEvent> objectType() {
        return DocumentFieldOptionAddedEvent.class;
    }

    @Override
    public Class<DocumentFieldOptionAdded> recordType() {
        return DocumentFieldOptionAdded.class;
    }
}
