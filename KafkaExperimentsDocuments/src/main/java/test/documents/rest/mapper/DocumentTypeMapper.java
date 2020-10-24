package test.documents.rest.mapper;

import test.documents.domain.DocumentType;
import test.documents.domain.published.PublishedDocumentType;
import test.documents.domain.published.PublishedDocumentTypeCode;

/**
 * @author s.smitienko
 */
public class DocumentTypeMapper {

    private DocumentTypeMapper() {
    }

    /**
     * Возвращает публичную модель типа документа
     *
     * @param documentType доменная модель типа документа
     * @return
     */
    public static PublishedDocumentType toPublished(DocumentType documentType) {
        return new PublishedDocumentType(documentType.identifier().id(),
                new PublishedDocumentTypeCode(documentType.documentTypeCode().code(),
                        documentType.documentTypeCode().classifierCode()),
                documentType.metadata().name(),
                documentType.metadata().description());
    }
}
