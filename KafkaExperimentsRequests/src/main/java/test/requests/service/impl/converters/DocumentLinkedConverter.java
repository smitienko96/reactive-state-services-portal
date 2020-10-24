package test.requests.service.impl.converters;

import org.springframework.stereotype.Component;
import test.common.domain.AggregateVersion;
import test.common.domain.DocumentId;
import test.common.domain.DocumentTypeCode;
import test.common.domain.DocumentTypeId;
import test.common.service.DomainEventConverter;
import test.requests.domain.ApplicantDocumentType;
import test.requests.domain.ApplicationNumber;
import test.requests.domain.DocumentFieldCode;
import test.requests.domain.events.DocumentLinkedEvent;
import test.requests.domain.events.avro.DocumentField;
import test.requests.domain.events.avro.DocumentLinked;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author s.smitienko
 */
@Component
public class DocumentLinkedConverter implements DomainEventConverter<DocumentLinkedEvent, DocumentLinked> {

    @Override
    public DocumentLinked toAvro(DocumentLinkedEvent domainEvent) {
        ApplicantDocumentType documentType = domainEvent.documentType();
        return DocumentLinked.newBuilder().setUuid(domainEvent.uuid().toString())
                .setDate(domainEvent.date().getTime())
                .setVersion(domainEvent.version().version())
                .setApplicationNumber(domainEvent.aggregateIdentifier().number())
                .setDocumentId(domainEvent.documentId().id())
                .setDesignation(domainEvent.designation())
                .setDocumentTypeId(documentType.typeId().id())
                .setDocumentTypeCode(documentType.typeCode().code())
                .setDocumentFields(domainEvent.documentFields().entrySet().stream().map(e ->
                        new DocumentField(e.getKey().flatCode(), e.getValue()))
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public DocumentLinkedEvent fromAvro(DocumentLinked record) {
        Map<DocumentFieldCode, String> documentFieldsMap = new HashMap<>();
        record.getDocumentFields().forEach(f -> documentFieldsMap.put(new DocumentFieldCode(f.getFieldCode()), f.getValue()));

        ApplicantDocumentType documentType = new ApplicantDocumentType(new DocumentTypeCode(record.getDocumentTypeCode()),
                new DocumentTypeId(record.getDocumentTypeId()));

        return new DocumentLinkedEvent(record.getDate(), record.getUuid(),
                new ApplicationNumber(record.getApplicationNumber()),
                new AggregateVersion(record.getVersion()),
                new DocumentId(record.getDocumentId()),
                documentType,
                record.getName(),
                record.getDescription(),
                record.getDesignation(),
                documentFieldsMap);
    }

    @Override
    public Class<DocumentLinkedEvent> objectType() {
        return DocumentLinkedEvent.class;
    }

    @Override
    public Class<DocumentLinked> recordType() {
        return DocumentLinked.class;
    }
}
