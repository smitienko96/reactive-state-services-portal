package test.documents.service;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import test.common.domain.AggregateNames;
import test.common.domain.DocumentId;
import test.common.domain.DocumentTypeId;
import test.common.service.MementoBasedAggregateRepository;
import test.documents.domain.Document;
import test.documents.domain.DocumentMementoBuilder;
import test.documents.domain.DocumentReferenceNumber;
import test.documents.domain.IDocumentsRepository;

import java.util.Collections;

@Component
public class DocumentsRepository extends MementoBasedAggregateRepository<Document, DocumentId> implements IDocumentsRepository {

    @Override
    public Flux<Document> getByDocumentType(DocumentTypeId documentType) {
        return getByFilter(Collections.singletonMap(
                DocumentMementoBuilder.DOCUMENT_TYPE_ID_FIELD, documentType.id()));
    }

    @Override
    public Flux<Document> getByReferenceNumber(DocumentReferenceNumber referenceNumber) {
        return getByFilter(Collections.singletonMap(
                DocumentMementoBuilder.REFERENCE_NUMBER_FIELD, referenceNumber.number()));
    }

    @Override
    protected String aggregateName() {
        return AggregateNames.DOCUMENTS;
    }
}
