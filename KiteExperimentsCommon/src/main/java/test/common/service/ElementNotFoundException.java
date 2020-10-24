package test.common.service;

/**
 * @author s.smitienko
 */
public class ElementNotFoundException extends RuntimeException {

    private String identifier;

    public ElementNotFoundException(String identifier) {
        super(String.format("No element found for identifier [%s]",
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
