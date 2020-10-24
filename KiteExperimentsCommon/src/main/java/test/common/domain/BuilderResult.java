package test.common.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public class BuilderResult<A extends DomainAggregate> {
    private A aggregate;
    private boolean saveRequired;
}
