package test.documents.rest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import test.common.domain.DocumentTypeId;
import test.common.service.CommandExecutor;
import test.documents.domain.DocumentType;
import test.documents.domain.DocumentTypeCode;
import test.documents.domain.IDocumentTypesRepository;
import test.documents.domain.published.*;
import test.documents.rest.mapper.DocumentFieldMapper;
import test.documents.rest.mapper.DocumentTypeMapper;
import test.documents.service.commandhandlers.RegisterDocumentFieldCommand;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/document-types")
@Slf4j
public class DocumentTypesController {

    @Autowired
    private IDocumentTypesRepository repository;

    @Autowired
    private CommandExecutor commandExecutor;

    @GetMapping("/{documentTypeId}")
    public Mono<PublishedDocumentType> getById(@PathVariable String documentTypeId) {
        return repository.get(new DocumentTypeId(documentTypeId)).map(DocumentTypeMapper::toPublished);
    }

    @GetMapping("/fields/{typeIdentifier}")
    public Flux<PublishedDocumentField> getDocumentFields(@PathVariable String typeIdentifier, @RequestParam("isCode") boolean isCode) {
        Mono<DocumentType> documentType = isCode ?
                repository.getByCode(new DocumentTypeCode(typeIdentifier)) :
                repository.get(new DocumentTypeId(typeIdentifier));
        return documentType.flatMapMany(t -> Flux.fromIterable(t.getAllDocumentFields()))
                .map(DocumentFieldMapper::toPublished);
    }

    @PutMapping("/fields")
    public Mono<Void> createDocumentField(@RequestBody RegisterDocumentFieldCommand fieldCommand) {
        return commandExecutor.execute(fieldCommand);
    }

    @PostMapping("/byCodes")
    public Flux<PublishedDocumentType> getByCodes(@RequestBody PublishedDocumentTypeCodesFilter typeCodesFilter) {
        List<PublishedDocumentTypeCode> typeCodes = typeCodesFilter.getDocumentTypeCodes();
        if (CollectionUtils.isEmpty(typeCodes)) {
            return Flux.empty();
        }
        List<Mono<PublishedDocumentType>> typesList = typeCodes.stream()
                .map(code -> repository.getByCode(new DocumentTypeCode(code.getCode(), code.getClassifierCode())).onErrorResume(t -> {
                    log.info("Skipping error [{}]", t.getMessage());
                    return Mono.empty();
                })
                        .map(DocumentTypeMapper::toPublished))
                .collect(Collectors.toList());
        return Flux.concat(typesList).switchIfEmpty(Mono.empty());
    }

    @PutMapping
    public Mono<Void> createDocumentType(@RequestBody CreateDocumentTypeCommand command) {
        return commandExecutor.execute(command);
    }

}
