package test.common.domain;

/**
 * @author s.smitienko
 */
public interface IPersistenceContextHolder<C> {
    C get();
}
