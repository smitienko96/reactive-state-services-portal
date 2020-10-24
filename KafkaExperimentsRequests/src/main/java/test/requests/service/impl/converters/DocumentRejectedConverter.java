package test.requests.service.impl.converters;

import org.springframework.stereotype.Component;
import test.common.domain.AggregateVersion;
import test.common.domain.DocumentId;
import test.common.domain.DocumentTypeId;
import test.common.service.DomainEventConverter;
import test.requests.domain.ApplicationNumber;
import test.requests.domain.DocumentFieldCode;
import test.requests.domain.events.DocumentRejectedEvent;
import test.requests.domain.events.avro.DocumentField;
import test.requests.domain.events.avro.DocumentRejected;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author s.smitienko
 */
@Component
public class DocumentRejectedConverter implements DomainEventConverter<DocumentRejectedEvent, DocumentRejected> {

    @Override
    public DocumentRejected toAvro(DocumentRejectedEvent domainEvent) {
        return DocumentRejected.newBuilder().setUuid(domainEvent.uuid().toString())
                .setDate(domainEvent.date().getTime())
                .setVersion(domainEvent.version().version())
                .setApplicationNumber(domainEvent.aggregateIdentifier().number())
                .setDocumentId(domainEvent.documentId().id())
                .setDocumentTypeId(domainEvent.documentTypeId().id())
                .setDocumentFields(domainEvent.documentFields().entrySet().stream().map(e ->
                        new DocumentField(e.getKey().flatCode(), e.getValue()))
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public DocumentRejectedEvent fromAvro(DocumentRejected record) {
        Map<DocumentFieldCode, String> documentFieldsMap = new HashMap<>();
        record.getDocumentFields().forEach(f -> documentFieldsMap.put(new DocumentFieldCode(f.getFieldCode()), f.getValue()));

        return new DocumentRejectedEvent(record.getDate(), record.getUuid(),
                new ApplicationNumber(record.getApplicationNumber()),
                new AggregateVersion(record.getVersion()),
                new DocumentId(record.getDocumentId()),
                new DocumentTypeId(record.getDocumentTypeId()),
                documentFieldsMap);
    }

    @Override
    public Class<DocumentRejectedEvent> objectType() {
        return DocumentRejectedEvent.class;
    }

    @Override
    public Class<DocumentRejected> recordType() {
        return DocumentRejected.class;
    }
}
