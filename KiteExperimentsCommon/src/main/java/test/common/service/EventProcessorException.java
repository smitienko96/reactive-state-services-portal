package test.common.service;

public class EventProcessorException extends Exception {


    public EventProcessorException(String message) {
        super(message);
    }

    public EventProcessorException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventProcessorException(Throwable cause) {
        super(cause);
    }
}
