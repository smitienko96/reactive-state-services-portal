package test.regulations.domain;

import lombok.EqualsAndHashCode;
import test.common.domain.AggregateIdentifier;

import java.util.Date;

@EqualsAndHashCode
public class RegulationIdentifier extends AggregateIdentifier {

    private long date;

    public RegulationIdentifier(String number, Date date) {
        super(number);
        this.date = date.getTime();
    }

    public String number() {
        return id();
    }

    public Date date() {
        return new Date(date);
    }

}
