package test.common.domain;

import lombok.*;

@EqualsAndHashCode(of = {"aggregateName", "identifier"})
@ToString
@Setter
@Getter
public abstract class DomainEvent<I extends AggregateIdentifier, T extends EventType> extends Event {


    protected I identifier;
    protected AggregateVersion version;

    public DomainEvent() {
       super();
    }

    public DomainEvent(long date, String uuid,
                       I identifier, AggregateVersion version) {
        super(date, uuid);
        this.identifier = identifier;
        this.version = version;
    }

    <A extends DomainAggregate<I>> void linkAggregate(A aggregate) {
        this.identifier = aggregate.identifier();
        this.version = aggregate.version();
    }

    public I aggregateIdentifier() {
        return identifier;
    }

    public AggregateVersion version() {
        return version;
    }

    /**
     * Наименование аг
     * @return
     */
    public abstract String aggregateName();

    public abstract T eventType();
}
