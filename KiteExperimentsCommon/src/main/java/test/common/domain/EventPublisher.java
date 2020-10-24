package test.common.domain;

import java.util.List;

public interface EventPublisher {

    /**
     *
     * @param event
     */
    void publish(DomainEvent event);

    /**
     *
     * @param aggregateName
     * @param identifier
     * @param events
     */
    void publish(String aggregateName,
                 AggregateIdentifier identifier, List<DomainEvent> events);

    /**
     *
     * @param ex
     */
    void publishError(Exception ex);
}
