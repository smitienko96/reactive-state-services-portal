package test.regulations.service.impl.commandhandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import test.common.domain.ApplicantType;
import test.common.domain.EventPublisher;
import test.common.service.NonReturningCommandHandler;
import test.regulations.domain.*;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RegisterRegulationCommandHandler implements NonReturningCommandHandler<RegisterRegulationCommand> {

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private IRegulationRepository regulationRepository;

    @Override
    public Mono<Void> handle(RegisterRegulationCommand command) {
        return checkCommandArguments(command)
                .<Regulation>then(Mono.create(sink -> sink.success(createRegulation(command))))
                .flatMap(regulation -> regulationRepository.save(regulation).doOnNext(success -> {
                    if (success) {
                        new RuntimeException(MessageFormat.format(
                                "Storing of regulation " +
                                        " with ID [{0}] did not " +
                                        "succeed!",
                                regulation.identifier().id()));
                    }
                }))
                .log()
                .checkpoint()
                .then();
    }

    private Mono<Void> checkCommandArguments(RegisterRegulationCommand command) {
        return Mono.empty();
    }

    private Regulation createRegulation(RegisterRegulationCommand command) {
        RegulationIdentifier identifier = new RegulationIdentifier(command.getNumber(), command.getDate());

        List<ApplicantType> applicantTypes = command.getAllowedApplicantTypes()
                .stream().map(t -> ApplicantType.valueOf(t.trim().toUpperCase())).collect(Collectors.toList());

        return new Regulation(eventPublisher, identifier, command.isActive(), command.isQuestionaryRequired(),
                new ServiceId(command.getServiceId()),
                new RegulationMetadata(command.getIssuedAuthority(),
                        command.getName(), command.getDescription()),
                applicantTypes);
    }

    @Override
    public Class<RegisterRegulationCommand> getCommandClass() {
        return RegisterRegulationCommand.class;
    }
}
