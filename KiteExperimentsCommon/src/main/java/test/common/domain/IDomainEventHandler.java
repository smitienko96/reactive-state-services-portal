package test.common.domain;

/**
 * @author s.smitienko
 */
public interface IDomainEventHandler<E extends DomainEvent>  {
        void handle(E event);
}
