package test.requests.domain;

import lombok.NoArgsConstructor;
import test.common.domain.AggregateIdentifier;

@NoArgsConstructor
public class ApplicantId extends AggregateIdentifier {

    public ApplicantId(String id) {
        super(id);
    }

}
