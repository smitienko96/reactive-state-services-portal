package test.requests.service.impl.converters;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;
import test.common.domain.AggregateVersion;
import test.common.service.DomainEventConverter;
import test.requests.domain.PassportData;
import test.requests.domain.SNILS;
import test.requests.domain.events.PassportDataSpecifiedEvent;
import test.requests.domain.events.avro.PassportDataSpecified;

import java.util.Date;

@Component
public class PassportDataSpecifiedConverter implements DomainEventConverter<PassportDataSpecifiedEvent, PassportDataSpecified> {

    @Override
    public PassportDataSpecifiedEvent fromAvro(PassportDataSpecified object) {

        String series = object.getSeries();
        String number = object.getNumber();
        Date issueDate = object.getIssueDate().toDate();
        String issuingAuthority = object.getIssuingAuthority();

        return new PassportDataSpecifiedEvent(object.getDate(), object.getUuid(),
                new SNILS(object.getSnils()),
                new AggregateVersion(object.getVersion()),
                new PassportData(series, number, issueDate, issuingAuthority));
    }

    @Override
    public Class<PassportDataSpecified> recordType() {
        return PassportDataSpecified.class;
    }

    @Override
    public PassportDataSpecified toAvro(PassportDataSpecifiedEvent object) {
        return PassportDataSpecified.newBuilder()
                .setDate(object.date().getTime())
                .setUuid(object.uuid().toString())
                .setVersion(object.version().version())
                .setSnils(object.aggregateIdentifier().snilsValue())
                .setSeries(object.passportData().series())
                .setNumber(object.passportData().number())
                .setIssueDate(LocalDate.fromDateFields(object.passportData().issueDate()))
                .setIssuingAuthority(object.passportData().issuingAuthority())
                .build();
    }

    @Override
    public Class<PassportDataSpecifiedEvent> objectType() {
        return PassportDataSpecifiedEvent.class;
    }
}
