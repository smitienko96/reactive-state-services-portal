package test.documents.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(of = {"value"})
@NoArgsConstructor
public class DocumentFieldOption implements Comparable<DocumentFieldOption> {

    private int order;

    private String value;
    private String description;

    public DocumentFieldOption(int order, String value) {
        this(order, value, "");
    }

    public DocumentFieldOption(int order, String value, String description) {
        this.order = order;
        this.value = value;
        this.description = description;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public int order() {
        return order;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public String value() {
        return value;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public String description() {
        return description;
    }

    @Override
    public int compareTo(DocumentFieldOption o) {
        return order - o.order;
    }
}
