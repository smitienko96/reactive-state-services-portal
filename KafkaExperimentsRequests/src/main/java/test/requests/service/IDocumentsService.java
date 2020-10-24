package test.requests.service;

import reactor.core.publisher.Flux;
import test.common.domain.DocumentTypeCode;
import test.requests.domain.ApplicantDocumentType;
import test.requests.domain.DocumentFieldCode;

import java.util.List;

public interface IDocumentsService {
    Flux<ApplicantDocumentType> getDocumentTypesForCodes(List<DocumentTypeCode> typeCodes);

    Flux<DocumentFieldCode> getFieldCodesForDocumentType(DocumentTypeCode documentType);
}
