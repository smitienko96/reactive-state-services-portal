package test.requests.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import test.common.service.CommandExecutor;
import test.requests.domain.IPublishedApplicantsRepository;
import test.requests.domain.published.PublishedIndividual;
import test.requests.domain.published.PublishedOrganization;
import test.requests.service.impl.commandhandlers.RegisterIndividualCommand;
import test.requests.service.impl.commandhandlers.SupplyPassportDataCommand;

@RestController
@RequestMapping("/applicants")
public class ApplicantsController {

    @Autowired
    private CommandExecutor commandExecutor;

    @Autowired
    private IPublishedApplicantsRepository applicantRepository;

    @GetMapping("/organizations")
    public Flux<PublishedOrganization> getOrganizations() {
        return applicantRepository.getOrganizations();
    }

    @GetMapping("/individuals")
    public Flux<PublishedIndividual> getIndividuals() {
        return applicantRepository.getIndividuals();
    }

    @PutMapping("/individuals")
    public Mono<Void> registerIndividual(@RequestBody RegisterIndividualCommand registerCommand) {
        return commandExecutor.execute(registerCommand);
    }

    @PostMapping("/individuals/passportData")
    public Mono<Void> supplyPassportData(@RequestBody SupplyPassportDataCommand passportDataCommand) {
        return commandExecutor.execute(passportDataCommand);
    }
}
