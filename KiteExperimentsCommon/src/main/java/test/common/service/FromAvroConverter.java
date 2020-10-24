package test.common.service;

import org.apache.avro.specific.SpecificRecord;

/**
 * @author s.smitienko
 */
public interface FromAvroConverter<R extends SpecificRecord, O> {

    O fromAvro(R object);

    Class<R> recordType();
}
