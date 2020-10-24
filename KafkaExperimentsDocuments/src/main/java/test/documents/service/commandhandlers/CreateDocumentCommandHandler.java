package test.documents.service.commandhandlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import test.common.domain.AggregateNames;
import test.common.domain.DocumentId;
import test.common.domain.DocumentTypeId;
import test.common.domain.EventPublisher;
import test.common.service.AggregateIdentifierGenerator;
import test.common.service.NonReturningCommandHandler;
import test.documents.domain.*;
import test.documents.domain.published.CreateDocumentCommand;

import java.text.MessageFormat;

@Component
@Slf4j
public class CreateDocumentCommandHandler implements NonReturningCommandHandler<CreateDocumentCommand> {

    @Autowired
    private EventPublisher publisher;

    @Autowired
    private IDocumentTypesRepository documentTypesRepository;

    @Autowired
    private IDocumentsRepository documentsRepository;

    @Autowired
    private AggregateIdentifierGenerator identifierGenerator;

    @Override
    public Mono<Void> handle(CreateDocumentCommand command) {
        return retrieveDocumentType(command.getDocumentTypeId())
                .map(type -> {
                    DocumentId documentId = identifierGenerator.generate(AggregateNames.DOCUMENTS);
                    return new Document(publisher,
                            documentId,
                            type,
                            new DocumentArtifactMetadata(command.getName(), command.getDescription()),
                            command.getReferenceNumber());
                }).flatMap(document -> documentsRepository.save(document).doOnSuccess(success -> {
                    if (!success) {
                        new RuntimeException(MessageFormat.format(
                                "Storing of document " +
                                        "with ID [{1}] did not " +
                                        "succeed!",
                                document.identifier().id()));
                    }
                }))
                .then()
                .checkpoint();
    }

    private Mono<DocumentType> retrieveDocumentType(String documentTypeId) {
        return documentTypesRepository.get(new DocumentTypeId(documentTypeId));
    }

    @Override
    public Class<CreateDocumentCommand> getCommandClass() {
        return CreateDocumentCommand.class;
    }
}
