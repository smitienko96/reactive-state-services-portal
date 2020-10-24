package test.documents.rest.mapper;

import test.documents.domain.Document;
import test.documents.domain.DocumentFieldDefinition;
import test.documents.domain.DocumentFieldType;
import test.documents.domain.published.PublishedDocumentField;

import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * @author s.smitienko
 */
public class DocumentFieldMapper {

    private DocumentFieldMapper() {
    }

    /**
     * Возвращает публичную модель поля документа
     *
     * @param documentField
     * @return
     */
    public static PublishedDocumentField toPublished(DocumentFieldDefinition documentField) {

        PublishedDocumentField published =
                new PublishedDocumentField(documentField.documentTypeId().id()
                        , constructPublishedFieldCode(documentField),
                        documentField.metadata().name(),
                        documentField.fieldType().name());

        published.setClassifierCode(documentField.fieldCode().classifierCode());

        if (DocumentFieldType.OPTIONS == documentField.fieldType()) {
            published.setOptions(documentField.options().options().stream()
                    .map(o -> o.value())
                    .collect(Collectors.toList()));

        }
        return published;
    }

    private static String constructPublishedFieldCode(DocumentFieldDefinition field) {
        return new StringJoiner(Document.TYPE_COMPONENTS_DELIMITER)
                .add(field.documentTypeCode().code())
                .add(field.fieldCode().code())
                .toString();
    }
}
