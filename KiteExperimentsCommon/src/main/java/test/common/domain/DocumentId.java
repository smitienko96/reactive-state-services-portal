package test.common.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
public class DocumentId extends AggregateIdentifier {

    public DocumentId(String id) {
        super(id);
    }
}
