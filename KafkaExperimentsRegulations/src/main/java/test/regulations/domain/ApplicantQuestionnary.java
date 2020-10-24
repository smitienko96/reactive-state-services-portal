package test.regulations.domain;

import test.common.domain.DocumentTypeId;

public class ApplicantQuestionnary {

    private DocumentTypeId documentTypeId;

    ApplicantQuestionnary(DocumentTypeId documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public DocumentTypeId documentTypeId() {
        return documentTypeId;
    }

}
