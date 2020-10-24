package test.requests.service.impl.commandhandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import test.common.service.CommandHandler;
import test.requests.domain.ApplicantDocumentService;
import test.requests.domain.ApplicantId;
import test.requests.domain.ApplicationType;
import test.requests.domain.published.HandoverQuestionnaryCommand;
import test.requests.domain.published.PublishedApplicantQuestionnary;

/**
 * @author s.smitienko
 */
@Component
public class HandoverQuestionaryCommandHandler implements CommandHandler<HandoverQuestionnaryCommand, PublishedApplicantQuestionnary> {

    @Autowired
    private ApplicantDocumentService applicantDocumentService;

    @Override
    public Mono<PublishedApplicantQuestionnary> handle(HandoverQuestionnaryCommand command) {
        return applicantDocumentService.handoverQuestionary(
                        new ApplicantId(command.getApplicantId()),
                        new ApplicationType(command.getApplicationType()));
    }

    @Override
    public Class<HandoverQuestionnaryCommand> getCommandClass() {
        return HandoverQuestionnaryCommand.class;
    }
}
