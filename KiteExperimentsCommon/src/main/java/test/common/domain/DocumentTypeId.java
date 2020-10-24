package test.common.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class DocumentTypeId extends AggregateIdentifier {

    public DocumentTypeId(String id) {
        super(id);
    }
}
