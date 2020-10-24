package test.documents.domain;

import test.common.domain.DocumentTypeId;

/**
 * @author s.smitienko
 */
public class DocumentFillService {

    private IDocumentTypesRepository typesRepository;

    public DocumentFillService(IDocumentTypesRepository typesRepository) {
        this.typesRepository = typesRepository;
    }

    /**
     * @param documentTypeId
     * @param fieldCode
     * @param value
     * @return
     */
    public boolean isValueSupportedForDocumentField(DocumentTypeId documentTypeId, DocumentFieldCode fieldCode, String value) {
        return (Boolean) typesRepository.get(documentTypeId)
                .map(t -> t.getDocumentField(fieldCode))
                .switchIfEmpty(null)
                .handle((field, sink) -> {
                    if (field == null) {
                        sink.next(false);
                    } else {
                        sink.next(field.isValueCandadateValid(value));
                    }
                    sink.complete();
                }).block();
    }
}
