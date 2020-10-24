package test.requests.domain;

import lombok.EqualsAndHashCode;
import test.common.domain.AggregateIdentifier;

@EqualsAndHashCode(callSuper = true)
public class ServiceId extends AggregateIdentifier {

    public ServiceId(String id) {
        super(id);
    }
}
