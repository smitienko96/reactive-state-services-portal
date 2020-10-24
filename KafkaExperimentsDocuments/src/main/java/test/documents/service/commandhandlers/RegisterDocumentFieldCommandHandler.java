package test.documents.service.commandhandlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import test.common.domain.DocumentTypeId;
import test.common.domain.EventPublisher;
import test.common.service.NonReturningCommandHandler;
import test.documents.domain.*;

/**
 * @author s.smitienko
 */
@Component
@Slf4j
public class RegisterDocumentFieldCommandHandler implements NonReturningCommandHandler<RegisterDocumentFieldCommand> {

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private IDocumentTypesRepository documentTypesRepository;


    @Override
    public Mono<Void> handle(RegisterDocumentFieldCommand command) {
        DocumentTypeId documentTypeId =
                new DocumentTypeId(command.getDocumentTypeId());
        return documentTypesRepository.get(documentTypeId)
                .<DocumentType>handle((documentType, sink) -> {
                    DocumentFieldCode code =
                            new DocumentFieldCode(command.getCode(),
                                    command.getClassifierCode());
                    DocumentFieldType type =
                            DocumentFieldType.valueOf(command.getDataType().toUpperCase());
                    DocumentArtifactMetadata metadata =
                            new DocumentArtifactMetadata(command.getName(),
                                    command.getDescription());

                    boolean success = (DocumentFieldType.OPTIONS == type) ?
                            documentType.registerDocumentFieldWithOptions(code, type, command.isRequired(), metadata, command.getOptions())
                            : documentType.registerDocumentField(code, type, command.isRequired(), metadata);

                    if (!success) {
                        sink.error(new RuntimeException("Document field was not registered"));
                        return;
                    }

                    sink.next(documentType);
                    sink.complete();
                })
                .flatMap(documentTypesRepository::save)
                .log()
                .then();
    }

    @Override
    public Class<RegisterDocumentFieldCommand> getCommandClass() {
        return RegisterDocumentFieldCommand.class;
    }
}
