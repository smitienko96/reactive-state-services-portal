package test.common.service;

import reactor.core.publisher.Mono;
import test.common.domain.AggregateIdentifier;
import test.common.domain.DomainAggregate;

public interface IAggregateStore<A extends DomainAggregate,
        I extends AggregateIdentifier> {

    Mono<A> read(I identifier);

    Mono<Boolean> put(A aggregate);

    Mono<Boolean> delete(I identifier);

}