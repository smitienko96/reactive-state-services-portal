package test.requests.domain.events;

import lombok.NoArgsConstructor;
import test.common.domain.AggregateCreatedEvent;
import test.common.domain.AggregateNames;
import test.requests.domain.RequestsEventType;
import test.requests.domain.SNILS;

@NoArgsConstructor
public class IndividualRegisteredEvent extends AggregateCreatedEvent<SNILS, RequestsEventType> {

    private String firstName;
    private String lastName;
    private String patronymic;

    public IndividualRegisteredEvent(String firstName, String lastName, String patronymic) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
    }

    public IndividualRegisteredEvent(long date, String uuid, SNILS identifier, String firstName, String lastName, String patronymic) {
        super(date, uuid, identifier);
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
    }

    public String firstName() { return firstName; }

    public String lastName() { return lastName; }

    public String patronymic() { return patronymic; }

    @Override
    public String aggregateName() {
        return AggregateNames.INDIVIDUALS;
    }

    @Override
    public RequestsEventType eventType() {
        return RequestsEventType.INDIVIDUAL_REGISTERED;
    }

}
