package test.common.service;

/**
 * @author s.smitienko
 */
public class ElementNotUniqueException extends RuntimeException {

    private String identifier;

    public ElementNotUniqueException(String identifier) {
        super(String.format("Duplicated elements found for identifier [%s]",
                identifier));
        this.identifier = identifier;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public String identifier() {
        return identifier;
    }
}
