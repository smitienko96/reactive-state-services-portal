package test.common.domain;

/**
 * @author s.smitienko
 */
public interface IAggregateMementoBuilder<A extends DomainAggregate> extends IMementoBuilder<A> {
    String getIdentifierKey();

    String getVersionKey();
}
