package test.requests.domain;

import lombok.NoArgsConstructor;
import test.common.domain.ValidatableValue;
import test.common.domain.ValidationException;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@NoArgsConstructor
public class PassportData implements ValidatableValue {

    private String series;
    private String number;
    private Date issueDate;
    private String issuingAuthority;

    public PassportData(@NotEmpty String series, @NotEmpty String number,
                        @NotNull Date issueDate, @NotEmpty String issuingAuthority) {
        this.series = series;
        this.number = number;
        this.issueDate = issueDate;
        this.issuingAuthority = issuingAuthority;

        validate();
    }

    public String series() {
        return series;
    }

    public String number() {
        return number;
    }

    public Date issueDate() {
        return new Date(issueDate.getTime());
    }

    public String issuingAuthority() {
        return issuingAuthority;
    }

    @Override
    public void validate() throws ValidationException {
        // TODO: implement
    }
}
