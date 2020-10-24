package test.regulations.domain;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import test.common.domain.IAggregateRepository;

@Repository
public interface IRegulationRepository extends IAggregateRepository<Regulation, RegulationIdentifier> {
    Mono<Regulation> getByServiceId(ServiceId serviceId);
}
