package test.documents.service.eventconverters;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import test.common.domain.AggregateVersion;
import test.common.domain.DocumentTypeId;
import test.common.service.DomainEventConverter;
import test.documents.domain.DocumentArtifactMetadata;
import test.documents.domain.DocumentFieldCode;
import test.documents.domain.DocumentFieldType;
import test.documents.domain.events.DocumentFieldRegisteredEvent;
import test.documents.domain.events.avro.DocumentFieldRegistered;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author s.smitienko
 */
@Component
public class DocumentFieldRegisteredConverter implements DomainEventConverter<DocumentFieldRegisteredEvent, DocumentFieldRegistered> {

    @Override
    public DocumentFieldRegistered toAvro(DocumentFieldRegisteredEvent domainEvent) {
        DocumentFieldRegistered.Builder builder =
                DocumentFieldRegistered.newBuilder()
                        .setUuid(domainEvent.uuid().toString())
                        .setDate(domainEvent.date().getTime())
                        .setDocumentTypeId(domainEvent.aggregateIdentifier().id())
                        .setVersion(domainEvent.version().version())
                        .setFieldCode(domainEvent.fieldCode().code())
                        .setClassifierCode(domainEvent.fieldCode().classifierCode())
                        .setFieldType(domainEvent.fieldType().name())
                        .setRequired(domainEvent.isRequired())
                        .setName(domainEvent.metadata().name())
                        .setDescription(domainEvent.metadata().description());

        Set<String> options = domainEvent.options();
        if (!CollectionUtils.isEmpty(options)) {
            builder.setOptions(options.stream().collect(Collectors.toList()));
        }

        return builder.build();
    }

    @Override
    public DocumentFieldRegisteredEvent fromAvro(DocumentFieldRegistered record) {
        Set<String> options = !CollectionUtils.isEmpty(record.getOptions()) ?
                record.getOptions().stream().collect(Collectors.toSet()) : null;

        return new DocumentFieldRegisteredEvent(record.getDate(),
                record.getUuid(),
                new DocumentTypeId(record.getDocumentTypeId()),
                new AggregateVersion(record.getVersion()),
                new DocumentFieldCode(record.getFieldCode(), record.getClassifierCode()),
                DocumentFieldType.valueOf(record.getFieldType()),
                record.getRequired(),
                new DocumentArtifactMetadata(record.getName(),
                        record.getDescription()), options);
    }

    @Override
    public Class<DocumentFieldRegisteredEvent> objectType() {
        return DocumentFieldRegisteredEvent.class;
    }

    @Override
    public Class<DocumentFieldRegistered> recordType() {
        return DocumentFieldRegistered.class;
    }
}
