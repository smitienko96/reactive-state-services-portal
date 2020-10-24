package test.common.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @author s.smitienko
 */
@Setter
@Getter
public abstract class AggregateCreatedEvent<I extends AggregateIdentifier,
        T extends EventType> extends DomainEvent<I, T> {

    public AggregateCreatedEvent() {
    }

    public AggregateCreatedEvent(long date, String uuid, I identifier) {
        super(date, uuid, identifier, AggregateVersion.initial());
    }
}
