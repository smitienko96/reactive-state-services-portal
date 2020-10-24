package test.common.service;

/**
 * @author s.smitienko
 */
public class AggregateConcurrentWriteException extends RuntimeException {


    public AggregateConcurrentWriteException() {
    }

    public AggregateConcurrentWriteException(String message) {
        super(message);
    }
}
