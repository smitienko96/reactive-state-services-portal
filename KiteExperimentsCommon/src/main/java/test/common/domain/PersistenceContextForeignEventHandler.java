package test.common.domain;

/**
 * @author s.smitienko
 */
public abstract class PersistenceContextForeignEventHandler<C extends IPersistenceContextHolder,
        E extends ForeignEvent> implements IForeignEventHandler<E> {

    private C contextHolder;

    protected PersistenceContextForeignEventHandler(C contextHolder) {
        this.contextHolder = contextHolder;
    }

    public final void handle(E event) {
        handle(event, contextHolder);
    }

    protected abstract void handle(E event, C context);

    public abstract Class<E> getEventClass();
}
