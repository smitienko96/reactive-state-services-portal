package test.common.service;

import org.apache.avro.specific.SpecificRecord;
import test.common.domain.ForeignEvent;

/**
 * @author s.smitienko
 */
public interface ForeignEventConverter<E extends ForeignEvent,
        R extends SpecificRecord> extends FromAvroConverter<R, E> {
}
