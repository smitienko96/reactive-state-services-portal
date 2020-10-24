package test.documents;

import org.springframework.beans.factory.annotation.Autowired;
import test.common.domain.DocumentTypeId;
import test.common.domain.EventPublisher;
import test.documents.domain.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author s.smitienko
 */
public class TestDataProvider {

    @Autowired
    private EventPublisher publisher;

    public List<DocumentType> documentTypeList() {
        List<DocumentType> result = new ArrayList<>();
        result.add(passport());
        result.add(employmentCard());
        return result;
    }

    private DocumentType passport() {
        DocumentType documentType = new DocumentType(publisher,
                new DocumentTypeId("CPST"),
                new DocumentTypeCode("PASSPORT",
                        "CITIZEN" +
                                ".PASSPORT"),
                new DocumentArtifactMetadata("Общегражданский паспорт",
                        "Паспорт " +
                                "гражданина Российской Федерации"), AttachmentPolicy.SINGLE_ATTACHMENT);

        documentType.registerDocumentField(
                new DocumentFieldCode("SERIES", "PASSPORT.SERIES"),
                DocumentFieldType.STRING, true, new DocumentArtifactMetadata(
                        "Серия паспорта"));

        documentType.registerDocumentField(
                new DocumentFieldCode("NUMBER", "PASSPORT.NUMBER"),
                DocumentFieldType.STRING, true, new DocumentArtifactMetadata(
                        "Номер паспорта"));

        documentType.registerDocumentField(
                new DocumentFieldCode("DATE", "ISSUE.DATE"),
                DocumentFieldType.DATE, true, new DocumentArtifactMetadata(
                        "Дата " +
                                "выдачи"));

        documentType.registerDocumentField(
                new DocumentFieldCode("AUTHORITY", "ISSUE.AUTHORITY"),
                DocumentFieldType.STRING, false, new DocumentArtifactMetadata(
                        "Орган, выдавший паспорт"));

        documentType.registerDocumentField(
                new DocumentFieldCode("GENDER", "CITIZEN.GENDER"),
                DocumentFieldType.OPTIONS, true, new DocumentArtifactMetadata(
                        "Пол " +
                                "гражданина"));

        DocumentFieldDefinition gender =
                documentType.getDocumentField(new DocumentFieldCode("GENDER"));
        gender.addOption(new DocumentFieldOption(0, "МУЖСКОЙ"));
        gender.addOption(new DocumentFieldOption(1, "ЖЕНСКИЙ"));

        return documentType;
    }

    private DocumentType employmentCard() {
        DocumentType documentType = new DocumentType(publisher,
                new DocumentTypeId("EMPC"),
                new DocumentTypeCode("EMPCARD",
                        "EMPLOYMENT" +
                                ".CARD"),
                new DocumentArtifactMetadata("Трудовая книжка",
                        "Трудовая книжка или ее дубликаты"),
                AttachmentPolicy.MULTIPLE_ATTACHMENT);

        documentType.registerDocumentField(
                new DocumentFieldCode("SERIES", "EMPLOYMENT.CARD.SERIES"),
                DocumentFieldType.STRING, true, new DocumentArtifactMetadata(
                        "Серия трудовой книжки"));
        documentType.registerDocumentField(
                new DocumentFieldCode("NUMBER", "EMPLOYMENT.CARD.NUMBER"),
                DocumentFieldType.STRING, true, new DocumentArtifactMetadata(
                        "Номер трудовой книжки"));
        documentType.registerDocumentField(new DocumentFieldCode("DATE"),
                DocumentFieldType.DATE, false, new DocumentArtifactMetadata(
                        "Дата " +
                                "выдачи трудовой книжки"));
        documentType.registerDocumentField(new DocumentFieldCode("ISSUER"),
                DocumentFieldType.STRING, false, new DocumentArtifactMetadata(
                        "ФИО " +
                                "выдавшего лица"));
        return documentType;

    }
}
