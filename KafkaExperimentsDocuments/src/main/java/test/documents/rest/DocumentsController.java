package test.documents.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import test.common.domain.DocumentId;
import test.common.domain.DocumentTypeId;
import test.common.service.CommandExecutor;
import test.documents.domain.Document;
import test.documents.domain.DocumentReferenceNumber;
import test.documents.domain.IDocumentsRepository;
import test.documents.domain.published.CreateDocumentCommand;

@RestController
@RequestMapping("/documents")
public class DocumentsController {

    @Autowired
    private IDocumentsRepository documentsRepository;

    @Autowired
    private CommandExecutor commandExecutor;

    @GetMapping("/{documentId}")
    public Mono<Document> getById(@PathVariable String documentId) {
        return documentsRepository.get(new DocumentId(documentId));
    }

    @GetMapping("/byDocumentType/{documentTypeId}")
    public Flux<Document> getByDocumentType(@PathVariable String documentTypeId) {
        return documentsRepository.getByDocumentType(new DocumentTypeId(documentTypeId));
    }

    @GetMapping("/byReferenceNumber/{referenceNumber}")
    public Flux<Document> getByReferenceNumber(@PathVariable String referenceNumber) {
        return documentsRepository.getByReferenceNumber(new DocumentReferenceNumber(referenceNumber));
    }

    @PutMapping
    public Mono<Void> createDocument(@RequestBody CreateDocumentCommand createDocument) {
        return commandExecutor.execute(createDocument);
    }
}
