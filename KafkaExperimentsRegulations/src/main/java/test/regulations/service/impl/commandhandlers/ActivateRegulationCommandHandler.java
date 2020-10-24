package test.regulations.service.impl.commandhandlers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import test.common.service.NonReturningCommandHandler;
import test.regulations.domain.IRegulationRepository;
import test.regulations.domain.Regulation;
import test.regulations.domain.RegulationIdentifier;
import test.regulations.domain.ServiceId;

/**
 * Активация регламента услуги в системе.
 */
@Component
public class ActivateRegulationCommandHandler implements NonReturningCommandHandler<ActivateRegulationCommand> {

    @Autowired
    private IRegulationRepository repository;

    @Override
    public Mono<Void> handle(ActivateRegulationCommand command) {

        final Mono<Regulation> regulation;
        if (!StringUtils.isBlank(command.getRegulationNumber()) && command.getRegulationDate() != null) {
            RegulationIdentifier identifier = new RegulationIdentifier(command.getRegulationNumber(), command.getRegulationDate());
            regulation = repository.get(identifier);
        } else if (!StringUtils.isBlank(command.getServiceId())) {
            regulation = repository.getByServiceId(new ServiceId(command.getServiceId()));
        } else {
            throw new RuntimeException("Invalid filter specified for domain command registration");
        }

        return regulation.doOnNext(r ->
                r.activateRegulation(command.getEnactmentDate()))
                .flatMap(repository::save)
                .checkpoint()
                .log()
                .then();
    }

    @Override
    public Class<ActivateRegulationCommand> getCommandClass() {
        return ActivateRegulationCommand.class;
    }
}
