package test.documents.service.eventconverters;

import org.springframework.stereotype.Component;
import test.common.domain.DocumentTypeId;
import test.common.service.DomainEventConverter;
import test.documents.domain.AttachmentPolicy;
import test.documents.domain.DocumentArtifactMetadata;
import test.documents.domain.DocumentTypeCode;
import test.documents.domain.UsageType;
import test.documents.domain.events.DocumentTypeCreatedEvent;
import test.documents.domain.events.avro.DocumentTypeCreated;

/**
 * @author s.smitienko
 */
@Component
public class DocumentTypeCreatedConverter implements DomainEventConverter<DocumentTypeCreatedEvent, DocumentTypeCreated> {
    @Override
    public DocumentTypeCreated toAvro(DocumentTypeCreatedEvent domainEvent) {
        return DocumentTypeCreated.newBuilder()
                .setUuid(domainEvent.uuid().toString())
                .setDate(domainEvent.date().getTime())
                .setDocumentTypeId(domainEvent.aggregateIdentifier().id())
                .setCode(domainEvent.documentTypeCode().code())
                .setClassifierCode(domainEvent.documentTypeCode().classifierCode())
                .setAttachmentPolicy(domainEvent.attachmentPolicy().name())
                .setName(domainEvent.metadata().name())
                .setDescription(domainEvent.metadata().description())
                .setUsageType(domainEvent.usageType().name())
                .build();
    }

    @Override
    public DocumentTypeCreatedEvent fromAvro(DocumentTypeCreated record) {
        return new DocumentTypeCreatedEvent(record.getDate(),
                record.getUuid(),
                new DocumentTypeId(record.getDocumentTypeId()),
                new DocumentTypeCode(record.getCode(),
                        record.getClassifierCode()),
                new DocumentArtifactMetadata(record.getName(),
                        record.getDescription()),
                AttachmentPolicy.valueOf(record.getAttachmentPolicy()),
                UsageType.valueOf(record.getUsageType()));
    }

    @Override
    public Class<DocumentTypeCreatedEvent> objectType() {
        return DocumentTypeCreatedEvent.class;
    }

    @Override
    public Class<DocumentTypeCreated> recordType() {
        return DocumentTypeCreated.class;
    }
}
