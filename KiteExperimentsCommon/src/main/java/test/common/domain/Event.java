package test.common.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

/**
 * @author s.smitienko
 */
@Setter
@Getter
public abstract class Event {

    private long date;
    private String uuid;
    private String correlationUuid;

    protected Event(long date, String uuid, String correlationUuid) {
        this(date, uuid);
        this.correlationUuid = correlationUuid;
    }

    protected Event() {
        this.date = new Date().getTime();
        this.uuid = UUID.randomUUID().toString();
    }

    protected Event(long date, String uuid) {
        this.date = date;
        this.uuid = uuid;
    }


    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public Date date() {
        return new Date(date);
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public UUID uuid() {
        return UUID.fromString(uuid);
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public UUID correlationUuid() {
        if (uuid == null) {
            return null;
        }
        return UUID.fromString(correlationUuid);
    }
}
