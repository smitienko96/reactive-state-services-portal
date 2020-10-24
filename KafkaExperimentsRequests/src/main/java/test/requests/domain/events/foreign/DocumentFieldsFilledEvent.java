package test.requests.domain.events.foreign;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import test.common.domain.ForeignEvent;

import java.util.Map;

/**
 * @author s.smitienko
 */
public class DocumentFieldsFilledEvent extends ForeignEvent {

    @EqualsAndHashCode(of = {"code"})
    @Getter
    @AllArgsConstructor
    public static class DocumentField {
        private String code;
        private String name;
        private String description;
        private String type;
    }

    private Map<DocumentField, String> documentFields;

    public DocumentFieldsFilledEvent(long date, String uuid, String documentId,
                                     Map<DocumentField, String> documentFields) {
        super(date, uuid, documentId, "");
        this.documentFields = documentFields;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public Map<DocumentField, String> documentFields() {
        return documentFields;
    }

    /**
     * @return
     */
    public String documentId() {
        return identity();
    }
}
