package test.regulations.domain;

/**
 * @author s.smitienko
 */
public class RegulationMetadata {

    private String name;
    private String description;
    private String issuedAuthority;

    public RegulationMetadata(String name,
                              String description, String issuedAuthority) {
        this.name = name;
        this.description = description;
        this.issuedAuthority = issuedAuthority;
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

    /**
     *
     * @return
     */
    public String issuedAuthority() { return issuedAuthority; }

}
