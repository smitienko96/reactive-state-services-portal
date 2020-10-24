package test.documents.domain;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import test.common.domain.DocumentTypeId;
import test.common.domain.IAggregateRepository;

@Repository
public interface IDocumentTypesRepository extends IAggregateRepository<DocumentType, DocumentTypeId> {
    Mono<DocumentType> getByCode(DocumentTypeCode code);
    Flux<DocumentType> getAll();
    Mono<Void> deleteAll();
}
