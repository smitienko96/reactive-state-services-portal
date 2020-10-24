package test.requests.service.impl.commandhandlers;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import test.common.domain.published.DomainCommand;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonClassDescription("Предоставление паспортных данных заявителя")
public class SupplyPassportDataCommand implements DomainCommand {

    @JsonProperty(required = true)
    private String applicantSNILS;

    @JsonProperty(required = true)
    private String passportSeries;

    @JsonProperty(required = true)
    private String passportNumber;

    @JsonProperty(required = true)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date issueDate;

    @JsonProperty(required = true)
    private String issuingAuthority;
}
