package test.regulations.service.impl.commandhandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import test.common.service.NonReturningCommandHandler;
import test.regulations.domain.IRegulationRepository;
import test.regulations.domain.RegulationIdentifier;

@Component
public class RegisterConditionOptionCommandHandler implements NonReturningCommandHandler<RegisterConditionOptionCommand> {

    @Autowired
    private IRegulationRepository repository;

    @Override
    public Mono<Void> handle(RegisterConditionOptionCommand command) {
        RegulationIdentifier identifier = new RegulationIdentifier(command.getRegulationNumber(), command.getRegulationDate());
        return repository.get(identifier)
                .doOnNext(r -> r.registerConditionOption(command))
                .flatMap(repository::save)
                .doOnNext(success -> {
                    if (!success) {
                        throw new RuntimeException(String.format(
                                "Unable to update regulation [%s] in repository",
                                identifier.toString()));
                    }
                })
                .checkpoint()
                .log()
                .then();
    }

    @Override
    public Class<RegisterConditionOptionCommand> getCommandClass() {
        return RegisterConditionOptionCommand.class;
    }
}
