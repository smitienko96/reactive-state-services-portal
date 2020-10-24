package test.requests.service.impl.converters.foreign;

import org.springframework.stereotype.Component;
import test.common.service.ForeignEventConverter;
import test.documents.domain.events.avro.DocumentCreated;
import test.requests.domain.ApplicationNumber;
import test.requests.domain.events.foreign.DocumentCreatedEvent;

/**
 * @author s.smitienko
 */
@Component
public class DocumentCreatedConverter implements ForeignEventConverter<DocumentCreatedEvent, DocumentCreated> {

    private static final String USAGE_TYPE_TO_FILTER = "EXTERNAL";
    @Override
    public DocumentCreatedEvent fromAvro(DocumentCreated object) {
        String usageType = object.getUsageType();

        if (!USAGE_TYPE_TO_FILTER.equals(usageType)) {
            return null;
        }
        ApplicationNumber applicationNumber = tryGetApplicationNumber(object);
        if (applicationNumber == null) {
            return null;
        }

        return new DocumentCreatedEvent(object.getDate(), object.getUuid(),
                object.getDocumentId(), object.getName(), object.getDescription(), applicationNumber);
    }

    private ApplicationNumber tryGetApplicationNumber(DocumentCreated event) {
        try {
            return new ApplicationNumber(event.getReferenceNumber());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
    @Override
    public Class<DocumentCreated> recordType() {
        return DocumentCreated.class;
    }
}
