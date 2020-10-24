package test.requests.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import test.common.domain.published.DomainCommand;
import test.common.service.CommandExecutor;
import test.requests.domain.Application;
import test.requests.domain.published.HandoverQuestionnaryCommand;
import test.requests.domain.published.PublishedApplicantQuestionnary;
import test.requests.domain.published.PublishedApplication;
import test.requests.rest.mapper.ApplicationMapper;
import test.requests.service.impl.commandhandlers.HandoverApplicationDraftCommand;

/**
 * @author s.smitienko
 */
@RestController
@RequestMapping("/questionaries")
public class QuestionariesController {

    @Autowired
    private CommandExecutor commandExecutor;

    @PostMapping("/{applicationType}/{applicantKind}")
//    @JsonView(ViewTypes.Prepare.class)
    public Mono<PublishedApplicantQuestionnary> handoverQuestionnary(@PathVariable String applicationType,
                                                                     @PathVariable String applicantKind,
                                                                     @RequestParam("applicantId") String applicantId) {

        return executeCommand(new HandoverQuestionnaryCommand(applicationType, applicantId, applicantKind),
                PublishedApplicantQuestionnary.class)
                .log()
                .checkpoint();
    }

    @PostMapping("/handoverDraft")
//    @JsonView(ViewTypes.Prepare.class)
    public Mono<PublishedApplication> handoverDraft(@RequestBody HandoverApplicationDraftCommand handoverCommand) {
        return executeCommand(handoverCommand, Application.class).map(ApplicationMapper::toPublished).checkpoint();
    }

    private <T> Mono<T> executeCommand(DomainCommand command, Class<T> clazz) {
        return commandExecutor.execute(command);
    }
}
