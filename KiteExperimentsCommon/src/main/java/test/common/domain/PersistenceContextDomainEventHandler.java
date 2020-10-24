package test.common.domain;

/**
 * @author s.smitienko
 */
public abstract class PersistenceContextDomainEventHandler<C extends IPersistenceContextHolder,
        E extends DomainEvent> implements IDomainEventHandler<E> {

    private C holder;

    protected PersistenceContextDomainEventHandler(C holder) {
        this.holder = holder;
    }

    public final void handle(E event) {
        handle(event, holder);
    }

    protected abstract void handle(E event, C holder);

    public abstract Class<E> getEventClass();
}
