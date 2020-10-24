package test.common.service;

import org.apache.avro.specific.SpecificRecord;
import test.common.domain.DomainEvent;

/**
 * @author s.smitienko
 */
public interface DomainEventConverter<E extends DomainEvent,
        R extends SpecificRecord> extends FromAvroConverter<R, E>,
        ToAvroConverter<E, R> {
}
