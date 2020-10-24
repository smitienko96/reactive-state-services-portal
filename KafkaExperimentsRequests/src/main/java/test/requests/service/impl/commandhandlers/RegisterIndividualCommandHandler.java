package test.requests.service.impl.commandhandlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import test.common.domain.EventPublisher;
import test.common.service.ElementNotUniqueException;
import test.common.service.NonReturningCommandHandler;
import test.requests.domain.IIndividualsRepository;
import test.requests.domain.Individual;
import test.requests.domain.IndividualBuilder;
import test.requests.domain.SNILS;

@Component
@Slf4j
public class RegisterIndividualCommandHandler implements NonReturningCommandHandler<RegisterIndividualCommand> {

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private IIndividualsRepository individualsRepository;

    @Override
    public Mono<Void> handle(RegisterIndividualCommand command) {
        SNILS snils = new SNILS(command.getSnils());
        return retrieveExisting(snils)
                .map(existing -> {
                    log.error("Already existing individual found for the same SNILS [{}] with name [{} {} {}]!",
                            snils.snilsValue(), existing.lastName(), existing.firstName(), existing.patronymic());
                    throw new ElementNotUniqueException(snils.snilsValue());
                })
                .doOnSuccess(d -> registerIndividual(snils, command))
                .then();

    }

    private Mono<Void> registerIndividual(SNILS snils, RegisterIndividualCommand command) {
        IndividualBuilder builder = IndividualBuilder.fresh(eventPublisher)
                .withSNILS(snils).withFirstName(command.getFirstName())
                .withLastName(command.getLastName())
                .withPatronymic(command.getPatronymic());

        Mono<Individual> result = Mono.just(builder.build().getAggregate());

        return result.then();
    }

    private Mono<Individual> retrieveExisting(SNILS applicantSnils) {
        return individualsRepository.get(applicantSnils);
    }

    @Override
    public Class<RegisterIndividualCommand> getCommandClass() {
        return RegisterIndividualCommand.class;
    }
}
