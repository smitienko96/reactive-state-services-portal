package test.requests.service.impl.converters.foreign;

import org.springframework.stereotype.Component;
import test.common.service.ForeignEventConverter;
import test.documents.domain.events.avro.DocumentField;
import test.documents.domain.events.avro.DocumentFieldsFilled;
import test.requests.domain.events.foreign.DocumentFieldsFilledEvent;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author s.smitienko
 */
@Component
public class DocumentFieldsFilledConverter implements ForeignEventConverter<DocumentFieldsFilledEvent, DocumentFieldsFilled> {

    @Override
    public DocumentFieldsFilledEvent fromAvro(DocumentFieldsFilled object) {
        Map<DocumentFieldsFilledEvent.DocumentField, String> documentFields =
                object.getDocumentFields().stream()
                        .collect(Collectors.toMap(f ->
                                        new DocumentFieldsFilledEvent.DocumentField(f.getFieldCode(),
                                                f.getName(), f.getDescription(), f.getType()),
                                DocumentField::getValue));

        return new DocumentFieldsFilledEvent(object.getDate(),
                object.getUuid(), object.getDocumentId(), documentFields);
    }

    @Override
    public Class<DocumentFieldsFilled> recordType() {
        return DocumentFieldsFilled.class;
    }
}
