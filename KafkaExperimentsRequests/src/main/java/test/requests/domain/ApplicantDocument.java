package test.requests.domain;

import test.common.domain.DocumentId;
import test.common.domain.DomainEntity;

public class ApplicantDocument extends DomainEntity<Application> {

    private DocumentId documentId;

    private ApplicantDocumentType documentType;

    private String name;
    private String description;

    private String designation;

    ApplicantDocument(Application application, DocumentId documentId,
                      ApplicantDocumentType documentType,
                      String name, String description, String designation) {
        super(application);
        this.documentId = documentId;
        this.documentType = documentType;
        this.name = name;
        this.description = description;
        this.designation = designation;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public String name() {
        return name;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public String description() {
        return description;
    }

    public String designation() { return designation; }

    public DocumentId documentId() {
        return documentId;
    }
}
