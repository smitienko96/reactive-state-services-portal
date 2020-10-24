package test.common.domain;

/**
 * Хранитель последней версии агрегата предметной области.
 */
public class LastVersionHolder {

    private AggregateVersion version;
    private boolean shadow;

    public LastVersionHolder(AggregateVersion version, boolean shadow) {
        this.version = version;
        this.shadow = shadow;
    }

    public static LastVersionHolder absent() {
        return new LastVersionHolder(AggregateVersion.absent(), false);
    }

    /**
     * Последняя версия агрегата
     *
     * @return
     */
    public AggregateVersion lastVersion() {
        return version;
    }

    /**
     * Является ли версия теневой (агрегат недоступен для чтения)
     *
     * @return
     */
    public boolean isShadow() {
        return shadow;
    }

}
