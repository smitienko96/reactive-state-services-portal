package test.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import test.common.domain.AggregateIdentifier;
import test.common.domain.DomainAggregate;
import test.common.domain.DomainEvent;
import test.common.domain.EventBasedAggregateUpdater;

/**
 * Кэширующее хранилище агрегатов, реализующее push-модель обновления их
 * состояний на основе событий предметной области.
 *
 * @author s.smitienko
 */
@Slf4j
@Component
public class CacheBasedUpdatableAggregateStore<A extends DomainAggregate,
        I extends AggregateIdentifier> extends CacheBasedAggregateStore<A, I> implements EventBasedAggregateUpdater<A> {

    @EventListener
    public <E extends DomainEvent<I, ?>> void handle(E event) {
        I identifier = event.aggregateIdentifier();
        A aggregate = read(identifier).block();
        if (aggregate == null) {
            log.info("Aggregate for ID: [{0}] not found in store. Skipping " +
                    "handling", identifier.id());
            return;
        }

        if (event.version().difference(aggregate.version()) > 1) {
            log.warn("Aggregate ID [{0}] retrieved from aggregate store is " +
                            "outdated. Aggregate version [{1}] against event version " +
                            "[{2}]. Projection won't be attempted!", identifier.id(),
                    aggregate.version(),
                    event.version());
            return;
        }

        log.info("Applying domain event [{0}] to aggregate [{1}]", event,
                aggregate);

        update(aggregate, event);
        log.info("Event applied");
    }

}
