package test.requests.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import test.common.domain.AggregateIdentifier;

@EqualsAndHashCode
@NoArgsConstructor
public class ApplicationNumber extends AggregateIdentifier {

    public static final String APPLICATION_NUMBER_PREFIX = "APP-";

    public ApplicationNumber(String id) {
        super(id);
    }

    public String number() {
        return id();
    }

    public boolean isValid(String id) {
        return super.isValid(id)
                && id.startsWith(APPLICATION_NUMBER_PREFIX);
    }
}
