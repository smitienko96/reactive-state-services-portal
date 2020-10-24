package test.requests.domain;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import test.common.domain.*;
import test.requests.domain.events.ApplicationDraftHandedOverEvent;
import test.requests.domain.events.ApplicationStatusChangedEvent;
import test.requests.domain.events.DocumentLinkedEvent;
import test.requests.domain.events.DocumentRejectedEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@AggregateMetadata(name = AggregateNames.APPLICATIONS, mode = AggregateMode.EVENT_SOURCING)
public class Application extends DomainAggregate<ApplicationNumber> {

    @Setter(AccessLevel.PACKAGE)
    private ApplicationType applicationType;

    @Setter(AccessLevel.PACKAGE)
    private Status status;

    @Setter(AccessLevel.PACKAGE)
    private ApplicantId applicant;

    @Setter(AccessLevel.PACKAGE)
    private OperatorId operator;

    @Setter(AccessLevel.PACKAGE)
    private List<ApplicantDocumentType> applicantDocumentTypes;

    private Map<DocumentId, ApplicantDocument> applicantDocuments = new HashMap<>();

    @Setter(AccessLevel.PACKAGE)
    private List<ApplicantDocumentField> applicantDocumentFields;

    /**
     * Конструктор восстановления существующего заявления
     *
     * @param publisher
     * @param identifier
     * @param version
     * @param applicationType
     * @param applicant
     * @param operator
     * @param applicantDocumentTypes
     * @param applicantDocumentField
     */
    Application(EventPublisher publisher, ApplicationNumber identifier, AggregateVersion version, ApplicationType applicationType,
                       ApplicantId applicant, OperatorId operator, List<ApplicantDocumentType> applicantDocumentTypes,
                       List<ApplicantDocumentField> applicantDocumentField) {
        super(publisher, identifier, version);
        this.applicationType = applicationType;
        this.status = Status.DRAFT;
        this.applicant = applicant;
        this.operator = operator;
        this.applicantDocumentTypes = applicantDocumentTypes;
        this.applicantDocuments = new HashMap<>();
        this.applicantDocumentFields = applicantDocumentField;
    }


    /**
     * @param documentId
     * @param documentName
     * @param documentDescription
     * @param designation
     * @param applicantDocumentType
     * @param documentFields
     */
    public void linkDocument(DocumentId documentId,
                             String documentName,
                             String documentDescription,
                             String designation,
                             ApplicantDocumentType applicantDocumentType,
                             Map<DocumentFieldCode, String> documentFields) {
        DocumentTypeId documentTypeId = applicantDocumentType.typeId();

        if (!checkDocumentType(documentTypeId, documentFields.keySet())) {
            raiseEvent(new DocumentRejectedEvent(documentId, documentTypeId, documentFields));
            return;
        }
        raiseEvent(new DocumentLinkedEvent(documentId, applicantDocumentType,
                documentName, documentDescription,
                designation,
                documentFields));
    }

    public Status status() { return status; }

    public ApplicationType applicationType() { return applicationType; }

    public ApplicantId applicant() { return applicant; }

    public OperatorId operator() { return operator; }


    private void changeStatus(Status newStatus) {
        if (!checkStatusChangeAllowed(newStatus)) {
            throw new RuntimeException("Status change invalid");
        }
        raiseEvent(new ApplicationStatusChangedEvent(newStatus));
    }

    private boolean checkStatusChangeAllowed(Status newStatus) {
        if (Status.DRAFT == newStatus) {
            return false;
        }
        if (Status.REGISTERED == status &&
                Status.RESULT_ISSUED != newStatus && Status.ARCHIVE != newStatus) {
            return false;
        }
        return true;
    }

    /**
     * @param linkedEvent
     */
    public void on(DocumentLinkedEvent linkedEvent) {

        Map<DocumentFieldCode, String> documentFields = linkedEvent.documentFields();
        DocumentId documentId = linkedEvent.documentId();

        final Map<DocumentFieldCode, ApplicantDocumentField> existing = applicantDocumentFields.stream().
                filter(field -> documentFields.keySet().contains(field.fieldCode()))
                .collect(Collectors.toMap(ApplicantDocumentField::fieldCode, v -> v));

        documentFields.forEach((code, value) -> {

            ensureApplicantDocumentExists(documentId,
                    linkedEvent.documentType(),
                    linkedEvent.name(),
                    linkedEvent.description(), linkedEvent.designation());

            ApplicantDocumentField
                    field = existing.get(code);
            createOrEditApplicantField(field, documentId, code, value);
        });

    }

    private void ensureApplicantDocumentExists(DocumentId documentId,
                                               ApplicantDocumentType documentType,
                                               String name, String description,
                                               String designation) {
        if (applicantDocuments.containsKey(documentId)) {
            return;
        }

        applicantDocuments.put(documentId, new ApplicantDocument(this,
                documentId, documentType, name, description, designation));
    }

    private void createOrEditApplicantField(ApplicantDocumentField field,
                                            DocumentId documentId,
                                            DocumentFieldCode code,
                                            String value) {
        if (field == null) {
            field = new ApplicantDocumentField(documentId, code, value);
            applicantDocumentFields.add(field);
        } else {
            field.linkDocument(documentId);
            field.changeValue(value);
        }
    }

    /**
     * @param statusChanged
     */
    public void on(ApplicationStatusChangedEvent statusChanged) {
        Status newStatus = statusChanged.newStatus();
        log.debug("Changing status to {0}", newStatus);
        this.status = newStatus;
    }

    public AggregateIdentifier applicationNumber() {
        return identifier();
    }

    private boolean checkDocumentType(DocumentTypeId documentType, Set<DocumentFieldCode> codes) {
        boolean typeIdOk = applicantDocumentTypes.stream().anyMatch(t -> t.typeId().equals(documentType));
        return typeIdOk && codes.stream()
                .allMatch(c -> applicantDocumentTypes.stream()
                        .anyMatch(t -> c.documentTypeCode().equals(t.typeCode())));
    }

    @Override
    public AggregateCreatedEvent createInitialEvent() {
        return new ApplicationDraftHandedOverEvent(applicationType,
                applicant, operator,
                applicantDocumentTypes, applicantDocumentFields);
    }

}
