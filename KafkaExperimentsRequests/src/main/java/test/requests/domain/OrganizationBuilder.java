package test.requests.domain;

import test.common.domain.AggregateBuilder;
import test.common.domain.AggregateVersion;
import test.common.domain.EventPublisher;

/**
 * Строитель организаций
 */
public class OrganizationBuilder extends AggregateBuilder<Organization> {

    private INN inn;

    private String shortName;
    private String longName;

    private OrganizationBuilder(EventPublisher publisher, AggregateVersion version) {
        super(publisher, version);
    }

    private OrganizationBuilder(EventPublisher publisher) {
        super(publisher);
    }

    public static OrganizationBuilder fresh(EventPublisher publisher) {
        return new OrganizationBuilder(publisher);
    }

    public static OrganizationBuilder restore(EventPublisher publisher, AggregateVersion version) {
        return new OrganizationBuilder(publisher, version);
    }

    public OrganizationBuilder withINN(INN inn) {
        this.inn = inn;
        return this;
    }

    public OrganizationBuilder withShortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public OrganizationBuilder withLongName(String longName) {
        this.longName = longName;
        return this;
    }

    @Override
    protected Organization assemblyFresh() {
        return assembly();
    }

    @Override
    protected Organization assemblyRestore() {
        return assembly();
    }

    private Organization assembly() {
        return new Organization(publisher, inn, version, shortName, longName);
    }
}
