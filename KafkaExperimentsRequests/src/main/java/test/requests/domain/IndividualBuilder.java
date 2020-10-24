package test.requests.domain;

import test.common.domain.AggregateBuilder;
import test.common.domain.AggregateVersion;
import test.common.domain.EventPublisher;

public class IndividualBuilder extends AggregateBuilder<Individual> {

    private SNILS snils;

    private String firstName;
    private String lastName;
    private String patronymic;

    private IndividualBuilder(EventPublisher publisher, AggregateVersion version) {
        super(publisher, version);
    }

    private IndividualBuilder(EventPublisher publisher) {
        super(publisher);
    }

    public static IndividualBuilder fresh(EventPublisher publisher) {
        return new IndividualBuilder(publisher);
    }

    public static IndividualBuilder restore(EventPublisher publisher, AggregateVersion version) {
        return new IndividualBuilder(publisher, version);
    }

    public IndividualBuilder withSNILS(SNILS snils) {
        this.snils = snils;
        return this;
    }

    public IndividualBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public IndividualBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public IndividualBuilder withPatronymic(String patronymic) {
        this.patronymic = patronymic;
        return this;
    }

    protected Individual assemblyFresh() {
        return assembly();
    }

    protected Individual assemblyRestore() {
        return assembly();
    }

    private Individual assembly() {
        return new Individual(publisher, snils, version,
                firstName, lastName, patronymic);
    }
}
