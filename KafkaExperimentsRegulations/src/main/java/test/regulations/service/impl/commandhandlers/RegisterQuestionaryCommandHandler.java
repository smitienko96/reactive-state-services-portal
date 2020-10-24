package test.regulations.service.impl.commandhandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import test.common.domain.DocumentTypeId;
import test.common.domain.EventPublisher;
import test.common.service.NonReturningCommandHandler;
import test.regulations.domain.IRegulationRepository;
import test.regulations.domain.RegulationIdentifier;

@Component
public class RegisterQuestionaryCommandHandler implements NonReturningCommandHandler<RegisterQuestionaryCommand> {

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private IRegulationRepository repository;

    @Override
    public Mono<Void> handle(RegisterQuestionaryCommand command) {
        RegulationIdentifier identifier = new RegulationIdentifier(command.getRegulationNumber(), command.getRegulationDate());
        return repository.get(identifier).doOnNext(regulation ->
                regulation.registerQuestionnary(new DocumentTypeId(command.getDocumentTypeId())))
                .flatMap(repository::save)
                .checkpoint()
                .log()
                .then();
    }

    @Override
    public Class<RegisterQuestionaryCommand> getCommandClass() {
        return RegisterQuestionaryCommand.class;
    }
}
