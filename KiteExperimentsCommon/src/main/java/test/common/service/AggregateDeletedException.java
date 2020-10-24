package test.common.service;

import test.common.domain.AggregateIdentifier;

import java.text.MessageFormat;

/**
 * Попытка работы с агрегатом, который был удален
 */
public class AggregateDeletedException extends RuntimeException {

    private String aggregateName;
    private AggregateIdentifier identifier;


    public AggregateDeletedException(String aggregateName, AggregateIdentifier identifier) {
        super(MessageFormat.format("Attempt was made to access aggregate [{}] with identifier" +
                " [{}] which is deleted", aggregateName, identifier));
        this.aggregateName = aggregateName;
        this.identifier = identifier;
    }

    public String aggregateName() {
        return aggregateName;
    }

    public AggregateIdentifier identifier() {
        return identifier;
    }
}
