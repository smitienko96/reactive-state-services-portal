package test.requests.domain.events.foreign;

import test.common.domain.ForeignEvent;
import test.requests.domain.ApplicationNumber;

/**
 * @author s.smitienko
 */
public class DocumentCreatedEvent extends ForeignEvent {

    private String name;
    private String description;
    private ApplicationNumber applicationNumber;

    public DocumentCreatedEvent(long date, String uuid, String documentId,
                                String name, String description, ApplicationNumber applicationNumber) {
        super(date, uuid, documentId, "");
        this.name = name;
        this.description = description;
        this.applicationNumber = applicationNumber;
    }

    public String documentId() {
        return identity();
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public String name() {
        return name;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public String description() {
        return description;
    }

    public ApplicationNumber applicationNumber() {
        return applicationNumber;
    }
}
