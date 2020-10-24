package test.common.domain;

/**
 * Интерфейс актуализатора состояния агрегата на основе события предметной
 * области
 *
 * @author s.smitienko
 */
public interface EventBasedAggregateUpdater<A extends DomainAggregate>{

    default void update(A aggregate, DomainEvent event) {
        aggregate.apply(event);
    }
}
