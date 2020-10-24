package test.requests.service.impl.commandhandlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import test.common.service.NonReturningCommandHandler;
import test.requests.domain.ApplicationNumber;
import test.requests.domain.IApplicationsRepository;

@Component
@Slf4j
public class ArchiveApplicationCommandHandler implements NonReturningCommandHandler<ArchiveApplicationCommand> {

    @Autowired
    private IApplicationsRepository repository;

    @Override
    public Mono<Void> handle(ArchiveApplicationCommand command) {
        return repository.delete(
                new ApplicationNumber(command.getApplicationNumber()))
                .doOnSuccess(success -> log.info("Application [{}] successfully archived",
                        command.getApplicationNumber()))
                .then();
    }

    @Override
    public Class<ArchiveApplicationCommand> getCommandClass() {
        return ArchiveApplicationCommand.class;
    }
}
