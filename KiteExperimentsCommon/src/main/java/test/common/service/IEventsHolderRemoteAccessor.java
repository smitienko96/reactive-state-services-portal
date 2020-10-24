package test.common.service;

import reactor.core.publisher.Mono;

public interface IEventsHolderRemoteAccessor {
    /**
     * @param aggregateName
     * @param identifier
     * @return
     */
    Mono<AggregateEventsHolder> getAggregateEventsHolder(String aggregateName, String identifier);
}
