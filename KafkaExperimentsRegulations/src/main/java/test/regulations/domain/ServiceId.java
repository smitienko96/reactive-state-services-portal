package test.regulations.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import test.common.domain.AggregateIdentifier;

import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode
@NoArgsConstructor
public class ServiceId extends AggregateIdentifier {

    public ServiceId(@NotEmpty String id) {
        super(id.toUpperCase());
    }
}

