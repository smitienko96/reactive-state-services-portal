package test.common.domain;

/**
 * Проектор, воссоздающий агрегат путем наигрывания (replay) на его первоначальное состояние потока произошедших с ним событий.
 *
 * @param <A> тип агрегата
 */
public class AggregateProjector<A extends DomainAggregate> implements EventBasedAggregateUpdater<A> {

    private A aggregate;
    private EventStream eventStream;

    public AggregateProjector(A initialState, EventStream eventStream) {
        this.aggregate = initialState;
        this.eventStream = eventStream;
    }

    /**
     * Восстанавливает агрегат из потока событий.
     *
     * @return восстановленный агрегат
     */
    public A project() {
        while (eventStream.hasNext()) {

            DomainEvent event = eventStream.next();

            if (event instanceof AggregateCreatedEvent
                    || event instanceof AggregateDeletedEvent) {
                continue;
            }

            update(aggregate, eventStream.next());
        }
        return aggregate;
    }
}
