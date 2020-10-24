package test.common.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import test.common.domain.AggregateIdentifier;
import test.common.domain.DomainAggregate;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author s.smitienko
 */
@Component
public class CacheBasedAggregateStore<A extends DomainAggregate,
        I extends AggregateIdentifier> implements IAggregateStore<A, I> {

    @Value("${aggregatestore.cache.threshold}")
    private Integer lastModifiedThreshold;

    private Map<I, A> aggregatesCache = new ConcurrentHashMap<>();

    @Override
    public Mono<A> read(I identifier) {
        return Mono.just(aggregatesCache.get(identifier));
    }

    @Override
    public Mono<Boolean> put(A aggregate) {
        return Mono.just(aggregatesCache.put((I) aggregate.identifier(),
                aggregate)).hasElement();
    }

    @Override
    public Mono<Boolean> delete(I identifier) {
        return Mono.just(aggregatesCache.remove(identifier)).hasElement();
    }

    @Scheduled(fixedDelay = 6000)
    private void removeExpired() {
        aggregatesCache.values().removeIf(a -> isDateExpired(a.lastModified()));
    }

    private boolean isDateExpired(Date date) {
        long dateMillis = date.getTime();
        long nowMillis = new Date().getTime();
        return (((nowMillis - dateMillis) / 60000) > lastModifiedThreshold);
    }
}