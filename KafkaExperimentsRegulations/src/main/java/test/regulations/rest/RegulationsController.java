package test.regulations.rest;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import test.common.domain.published.DomainCommand;
import test.common.service.CommandExecutor;
import test.documents.domain.published.PublishedConditionSelection;
import test.documents.domain.published.PublishedDocumentTypeCode;
import test.documents.domain.published.PublishedServiceConditionsSelection;
import test.regulations.domain.*;
import test.regulations.domain.published.PublishedRegulation;
import test.regulations.domain.published.PublishedRegulationCondition;
import test.regulations.rest.mapper.RegulationConditionMapper;
import test.regulations.rest.mapper.RegulationMapper;
import test.regulations.service.impl.commandhandlers.*;

import javax.ws.rs.NotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/regulations")
public class RegulationsController {

    @Autowired
    private IRegulationRepository repository;

    @Autowired
    private CommandExecutor commandExecutor;

    @GetMapping("/{serviceId}")
    public Mono<PublishedRegulation> getByServiceId(@PathVariable String serviceId) {
        return repository.getByServiceId(new ServiceId(serviceId))
                .map(RegulationMapper::toPublished).checkpoint().log();
    }

    @GetMapping("/conditions/{regulationNumber}")
    public Flux<PublishedRegulationCondition> getRegulationConditions(@PathVariable String regulationNumber,
                                                                      @RequestParam("regulationDate") String regulationDate) {
        RegulationIdentifier identifier = parseRegulationIdentifier(regulationNumber, regulationDate);
        return repository.get(identifier)
                .flatMapIterable(r -> r.conditions())
                .map(RegulationConditionMapper::toPublished)
                .checkpoint()
                .log();
    }

    private RegulationIdentifier parseRegulationIdentifier(String number, String dateString) {
        try {
            Date regulationDate = new SimpleDateFormat("dd-MM-yyyy").parse(dateString);
            return new RegulationIdentifier(number, regulationDate);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    @PostMapping("/applicant-documents")
    public Flux<PublishedDocumentTypeCode> getDocumentTypesForOptions(@RequestBody PublishedServiceConditionsSelection conditions) {
        String serviceId = conditions.getServiceId();
        Mono<Regulation> regulation =
                repository.getByServiceId(new ServiceId(serviceId));

        List<PublishedConditionSelection> conditionSelections = conditions.getConditions();

        return regulation.flatMapIterable(r -> {
            if (r == null) {
                throw new NotFoundException(String.format("Regulation for service %s not found", serviceId));
            }

            if (CollectionUtils.isEmpty(conditionSelections)) {
                return r.allPossibleDocumentTypes();
            }

            Map<ConditionCode, ConditionOptionCode> optionsMap = buildOptionsMap(conditionSelections);
            return r.documentTypesForOptions(optionsMap);

        }).map(doctype -> new PublishedDocumentTypeCode(doctype.code()));
    }

    private Map<ConditionCode, ConditionOptionCode> buildOptionsMap(List<PublishedConditionSelection> conditionSelections) {
        return conditionSelections.stream()
                .collect(Collectors.toMap(k -> new ConditionCode(k.getConditionCode()),
                        v -> new ConditionOptionCode(v.getConditionOptionCode())));
    }

    @PutMapping
    public Mono<Void> registerRegulation(@RequestBody RegisterRegulationCommand command) {
        return executeCommand(command);
    }

    @PutMapping("/condition")
    public Mono<Void> registerConditionOption(@RequestBody RegisterConditionOptionCommand command) {
        return executeCommand(command);
    }

    @PostMapping("/activate")
    public Mono<Void> activateRegulation(@RequestBody ActivateRegulationCommand command) {
        return executeCommand(command);
    }

    @PostMapping("/deactivate")
    public Mono<Void> deactivateRegulation(@RequestBody DeactivateRegulationCommand command) {
        return executeCommand(command);
    }

    @PostMapping("/registerQuestionary")
    public Mono<Void> registerQuestionary(@RequestBody RegisterQuestionaryCommand command) {
        return executeCommand(command);
    }

    private Mono<Void> executeCommand(DomainCommand command) {
        return commandExecutor.execute(command);
    }

}
