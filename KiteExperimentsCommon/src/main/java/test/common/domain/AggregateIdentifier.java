package test.common.domain;

import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@ToString
@NoArgsConstructor
public class AggregateIdentifier {
    private String id;

    public AggregateIdentifier(String id) {
        if (!isValid(id)) {
            throw new RuntimeException(String.format("Identifier [%s] is not valid for aggregate [%s]",
                    id, getClass().getSimpleName()));
        }
        this.id = id;
    }

    public String id() {
        return id;
    }

    public boolean isValid(String id) {
        return !StringUtils.isBlank(id);
    }
}

