package test.documents.domain;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import test.common.domain.DocumentId;
import test.common.domain.DocumentTypeId;
import test.common.domain.IAggregateRepository;

@Repository
public interface IDocumentsRepository extends IAggregateRepository<Document,
        DocumentId> {

    Flux<Document> getByDocumentType(DocumentTypeId documentType);

    Flux<Document> getByReferenceNumber(DocumentReferenceNumber referenceNumber);
}
