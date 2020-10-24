package test.requests.domain;

import lombok.extern.slf4j.Slf4j;
import test.common.domain.*;
import test.requests.domain.events.IndividualAccountActivatedEvent;
import test.requests.domain.events.IndividualRegisteredEvent;
import test.requests.domain.events.PassportDataSpecifiedEvent;

import java.util.Date;

@Slf4j
@AggregateMetadata(name = AggregateNames.INDIVIDUALS, mode = AggregateMode.EVENT_SOURCING)
public class Individual extends Applicant<SNILS> {

    private String firstName;
    private String lastName;
    private String patronymic;

    private PassportData passportData;
    private boolean active = false;

    /**
     * Конструктор регистрации нового физического лица
     *
     * @param publisher
     * @param identifier
     * @param firstName
     * @param lastName
     * @param patronymic
     */
    Individual(EventPublisher publisher, SNILS identifier, String firstName, String lastName, String patronymic) {
        super(publisher, identifier, ApplicantType.PHYSICAL);
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
    }

    /**
     * Конструктор восстановления существующего физического лица
     *
     * @param publisher
     * @param identifier
     * @param version
     * @param firstName
     * @param lastName
     * @param patronymic
     */
    Individual(EventPublisher publisher, SNILS identifier, AggregateVersion version,
               String firstName, String lastName, String patronymic) {
        super(publisher, identifier, version, ApplicantType.PHYSICAL);
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
    }

    /**
     *
     * @param series
     * @param number
     * @param issueDate
     * @param issuingAuthority
     */
    public void supplyPassportData(String series, String number, Date issueDate, String issuingAuthority) {
        PassportData passportData = new PassportData(series, number, issueDate, issuingAuthority);

        raiseEvent(new PassportDataSpecifiedEvent(passportData));

        checkActivateAccount();
    }

    /**
     * Проверяет, может ли заявитель подавать заявления в госорган.
     * @return
     */
    public boolean canSubmitApplications() {
        return active;
    }

    private void checkActivateAccount() {
        if (!active) {
            raiseEvent(new IndividualAccountActivatedEvent());
        }
    }

    /**
     * @param passportDataSpecified
     */
    private void on(PassportDataSpecifiedEvent passportDataSpecified) {
        this.passportData = passportDataSpecified.passportData();
    }

    /**
     *
     * @param accountActivated
     */
    private void on(IndividualAccountActivatedEvent accountActivated) {
        this.active = true;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public String patronymic() {
        return patronymic;
    }

    public SNILS snils() {
        return identifier();
    }

    public PassportData passportData() {
        return passportData;
    }

    @Override
    public IndividualRegisteredEvent createInitialEvent() {
        return new IndividualRegisteredEvent(firstName, lastName, patronymic);
    }

}
