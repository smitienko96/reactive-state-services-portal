package test.common.domain;

import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import test.common.service.AggregateConcurrentWriteException;
import test.common.service.IEventStore;

/**
 * Реализация репозитория, воссоздающая состояние агрегата из потока его доменных событий.
 * Является воплощением паттерна Event Sourcing.
 *
 * @param <A> тип агрегата
 * @param <I> тип идентификатора агрегата
 * @param <E> тип события первоначального создания агрегата
 * @see AggregateProjector
 */
public abstract class ProjectingAggregateRepository<A extends DomainAggregate<I>, I extends AggregateIdentifier,
        E extends AggregateCreatedEvent<I, ?>> implements IAggregateRepository<A, I> {


    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private IEventStore eventStore;

    @Override
    public Mono<A> get(I identifier) {
        Mono<DomainEvent> initialEvent =
                eventStore.getInitialEvent(aggregateName(), identifier);
        Mono<LastVersionHolder> lastVersionHolder = eventStore.getLastVersion(aggregateName(), identifier);

        Mono<A> initialState = lastVersionHolder
                // treat shadow aggregates as absent ones
                .filter(h -> !h.isShadow())
                .map(LastVersionHolder::lastVersion)
                .zipWith(initialEvent, (version, event) ->
                        createInitialState((E) event, version, eventPublisher));

        Mono<EventStream> eventStream = eventStore.loadFullEventStream(aggregateName(),
                identifier);

        return
                initialState
                        .zipWith(eventStream,
                                (initial, stream) ->
                                        new AggregateProjector<>(initial, stream).project())
                        .switchIfEmpty(Mono.empty());
    }

    @Override
    public Mono<Boolean> doSave(A aggregate) {
        return Mono.error(new RuntimeException(
                String.format("Save not supported for event sourced aggregates [%s]",
                        aggregate.aggregateName())));
    }

    @Override
    public Mono<Boolean> doDelete(A aggregate) {
        AggregateDeletedEvent<A, I, ?> aggregateDeletedEvent = createAggregateDeletedEvent(aggregate);
        return eventStore.appendToStream(aggregateDeletedEvent.aggregateName(),
                aggregateDeletedEvent.aggregateIdentifier(), aggregateDeletedEvent)
                .thenReturn(true)
                .onErrorResume(AggregateConcurrentWriteException.class, e -> Mono.just(false));
    }


    protected abstract <D extends AggregateDeletedEvent<A, I, ?>> D createAggregateDeletedEvent(A aggregate);

    /**
     * Имя агрегата предметной области
     *
     * @return
     */
    protected abstract String aggregateName();

    /**
     * Создает первоначальное состояние агрегата из события его создания
     *
     * @param initialEvent событие создания агрегата
     * @param lastVersion  последняя версия агрегата предметной области
     * @param publisher    публикатор событий предметной области
     * @return
     */
    protected abstract A createInitialState(E initialEvent, AggregateVersion lastVersion, EventPublisher publisher);
}
