package test.requests.service.impl.commandhandlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import test.common.service.CommandHandler;
import test.requests.domain.*;

/**
 * @author s.smitienko
 */
@Component
@Slf4j
public class HandoverApplicationDraftCommandHandler implements CommandHandler<HandoverApplicationDraftCommand, Application> {

    @Autowired
    private IIndividualsRepository individualsRepository;

    @Autowired
    private IOrganizationsRepository organizationsRepository;

    @Autowired
    private ApplicantDocumentService applicantService;

    @Override
    public Mono<Application> handle(HandoverApplicationDraftCommand command) {
        return checkApplicantValid(command.getApplicantId())
                .flatMap(valid -> {
                    if (!valid) {
                        throw new RuntimeException(String.format("Neither individual nor organization" +
                                        " for identifier [%s] is eligible to submit applications",
                                command.getApplicantId()));
                    }
                    return applicantService.handoverDraft(new ApplicantId(command.getApplicantId()), command.getQuestionnary());
                });
    }

    private Mono<Boolean> checkApplicantValid(String applicantId) {
        Mono<Boolean> individualValid
                = individualsRepository.get(new SNILS(applicantId))
                .map(individual -> {
                    boolean valid = individual.canSubmitApplications();
                    if (!valid) {
                        log.info("Individual [{}] with SNILS [{}] couldn't submit " +
                                "applications to authorities", individual.lastName(), individual.snils());
                    }
                    return valid;
                })
                .defaultIfEmpty(false);

        Mono<Boolean> organizationValid =
                organizationsRepository.get(new INN(applicantId))
                        .map(organization -> true)
                        .defaultIfEmpty(false);

        return individualValid.or(organizationValid);
    }

    @Override
    public Class<HandoverApplicationDraftCommand> getCommandClass() {
        return HandoverApplicationDraftCommand.class;
    }
}
