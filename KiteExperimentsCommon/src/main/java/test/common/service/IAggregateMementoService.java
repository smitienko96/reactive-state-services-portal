package test.common.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import test.common.domain.Memento;

import java.util.Map;

/**
 * @author s.smitienko
 */
public interface IAggregateMementoService {

    Flux<Memento> getMemento(String aggregateName,
                             Map<FilterAttribute, Object> filterAttributes);

    Mono<Boolean> storeMemento(String aggregateName, Map<FilterAttribute, Object> filterAttributes,
                            Memento memento);

    Mono<Void> deleteMemento(String aggregateName, Map<FilterAttribute, Object> filterAttributes);
}
