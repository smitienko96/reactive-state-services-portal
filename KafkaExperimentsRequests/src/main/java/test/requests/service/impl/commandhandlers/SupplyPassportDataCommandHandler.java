package test.requests.service.impl.commandhandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import test.common.service.CircuitBreakerHelper;
import test.common.service.NonReturningCommandHandler;
import test.requests.domain.IIndividualsRepository;
import test.requests.domain.SNILS;

@Component
public class SupplyPassportDataCommandHandler implements NonReturningCommandHandler<SupplyPassportDataCommand> {

    @Autowired
    private IIndividualsRepository repository;

    @Override
    public Mono<Void> handle(SupplyPassportDataCommand command) {
        return CircuitBreakerHelper.wrapWithHystrix(
                repository.get(new SNILS(command.getApplicantSNILS()))
                        .doOnNext(applicant ->
                                applicant.supplyPassportData(command.getPassportSeries(), command.getPassportNumber(),
                                        command.getIssueDate(), command.getIssuingAuthority()))
                ,
                getCommandClass().getSimpleName(),
                Mono.empty())
                .then();
    }

    @Override
    public Class<SupplyPassportDataCommand> getCommandClass() {
        return SupplyPassportDataCommand.class;
    }
}
