package test.common.domain;

/**
 * @author s.smitienko
 */
public abstract class ForeignEvent extends Event {

    private String identity;

    public ForeignEvent(long date, String uuid, String identity,
                        String correlationUuid) {
        super(date, uuid, correlationUuid);
        this.identity = identity;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public String identity() {
        return identity;
    }
}
