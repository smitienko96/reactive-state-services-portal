package test.requests.service.impl.converters;

import org.springframework.stereotype.Component;
import test.common.domain.DocumentId;
import test.common.domain.DocumentTypeCode;
import test.common.domain.DocumentTypeId;
import test.common.service.DomainEventConverter;
import test.requests.domain.*;
import test.requests.domain.events.ApplicationDraftHandedOverEvent;
import test.requests.domain.events.avro.ApplicantDocumentField;
import test.requests.domain.events.avro.ApplicantDocumentType;
import test.requests.domain.events.avro.ApplicationDraftHandedOver;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author s.smitienko
 */
@Component
public class ApplicationDraftHandedOverConverter implements DomainEventConverter<ApplicationDraftHandedOverEvent, ApplicationDraftHandedOver> {

    @Override
    public ApplicationDraftHandedOver toAvro(ApplicationDraftHandedOverEvent domainEvent) {
        return ApplicationDraftHandedOver.newBuilder()
                .setUuid(domainEvent.uuid().toString())
                .setDate(domainEvent.date().getTime())
                .setVersion(domainEvent.version().version())
                .setApplicationNumber(domainEvent.aggregateIdentifier().number())
                .setApplicationType(domainEvent.applicationType().typeCode())
                .setApplicantId(domainEvent.applicantId().id())
                .setOperatorId(domainEvent.operatorId().id())
                .setApplicantDocumentFields(domainEvent.documentFields().stream().map(f ->
                        new ApplicantDocumentField(f.documentId().id(), f.fieldCode().flatCode(), f.fieldValue()))
                        .collect(Collectors.toList()))
                .setApplicantDocumentTypes(domainEvent.documentTypes().stream().map(t ->
                        new ApplicantDocumentType(t.typeCode().code(), t.typeId().id())).collect(Collectors.toList()))
                .build();
    }

    @Override
    public ApplicationDraftHandedOverEvent fromAvro(ApplicationDraftHandedOver record) {
        List<test.requests.domain.ApplicantDocumentField> documentFields =
                record.getApplicantDocumentFields().stream().map(f -> new test.requests.domain.ApplicantDocumentField(
                        new DocumentId(f.getDocumentId()), new DocumentFieldCode(f.getFieldCode()), f.getFieldValue()))
                        .collect(Collectors.toList());
        List<test.requests.domain.ApplicantDocumentType> documentTypes =
                record.getApplicantDocumentTypes().stream().map(t -> new test.requests.domain.ApplicantDocumentType(
                        new DocumentTypeCode(t.getTypeCode()), new DocumentTypeId(t.getTypeId())))
                        .collect(Collectors.toList());

        return new ApplicationDraftHandedOverEvent(record.getDate(),
                record.getUuid(),
                new ApplicationNumber(record.getApplicationNumber()),
                new ApplicationType(record.getApplicationType()),
                new ApplicantId(record.getApplicantId()),
                new OperatorId(record.getOperatorId()), documentTypes,
                documentFields);
    }

    @Override
    public Class<ApplicationDraftHandedOverEvent> objectType() {
        return ApplicationDraftHandedOverEvent.class;
    }

    @Override
    public Class<ApplicationDraftHandedOver> recordType() {
        return ApplicationDraftHandedOver.class;
    }
}
