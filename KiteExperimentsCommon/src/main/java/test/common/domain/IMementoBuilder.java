package test.common.domain;

/**
 * @author s.smitienko
 */
public interface IMementoBuilder<A> {

    Memento backup(A entity);

    A restore(Memento memento);
}
