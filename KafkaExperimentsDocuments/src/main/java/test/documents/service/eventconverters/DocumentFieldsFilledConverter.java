package test.documents.service.eventconverters;

import org.springframework.stereotype.Component;
import test.common.domain.AggregateVersion;
import test.common.domain.DocumentId;
import test.common.service.DomainEventConverter;
import test.documents.domain.DocumentFieldCode;
import test.documents.domain.events.DocumentFieldsFilledEvent;
import test.documents.domain.events.avro.DocumentField;
import test.documents.domain.events.avro.DocumentFieldsFilled;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author s.smitienko
 */
@Component
public class DocumentFieldsFilledConverter implements DomainEventConverter<DocumentFieldsFilledEvent, DocumentFieldsFilled> {

    @Override
    public DocumentFieldsFilled toAvro(DocumentFieldsFilledEvent object) {

        List<DocumentField> documentFields = object.documentFields()
                .entrySet().stream().map(e -> new DocumentField(e.getKey().code()
                        , e.getValue())).collect(Collectors.toList());

        return DocumentFieldsFilled.newBuilder()
                .setDate(object.date().getTime())
                .setUuid(object.uuid().toString())
                .setVersion(object.version().version())
                .setDocumentId(object.aggregateIdentifier().id())
                .setDocumentFields(documentFields)
                .build();
    }

    @Override
    public DocumentFieldsFilledEvent fromAvro(DocumentFieldsFilled object) {
        Map<DocumentFieldCode, String> documentFields =
                object.getDocumentFields().stream()
                        .collect(Collectors.toMap(k -> new DocumentFieldCode(k.getFieldCode()), v -> v.getValue()));

        return new DocumentFieldsFilledEvent(object.getDate(),
                object.getUuid(), new DocumentId(object.getDocumentId()),
                new AggregateVersion(object.getVersion()), documentFields);
    }

    @Override
    public Class<DocumentFieldsFilled> recordType() {
        return DocumentFieldsFilled.class;
    }


    @Override
    public Class<DocumentFieldsFilledEvent> objectType() {
        return DocumentFieldsFilledEvent.class;
    }
}
