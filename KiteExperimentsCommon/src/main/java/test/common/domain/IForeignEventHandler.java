package test.common.domain;

/**
 * @author s.smitienko
 */
public interface IForeignEventHandler<E extends ForeignEvent> {
    void handle(E event);
}
