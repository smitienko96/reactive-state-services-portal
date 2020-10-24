package test.regulations.service.impl.commandhandlers;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import test.common.domain.published.DomainCommand;

import java.util.Date;
import java.util.List;

@JsonClassDescription("Команда регистрации нового регламента услуги")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
public class RegisterRegulationCommand implements DomainCommand {

    @JsonProperty(required = true)
    private String number;

    @JsonFormat(pattern = "dd-MM-yyyy", shape = JsonFormat.Shape.STRING)
    @JsonProperty(required = true)
    private Date date;

    @JsonProperty(required = true)
    private String serviceId;

    @JsonProperty(required = true)
    private String name;

    @JsonProperty(required = true)
    private String description;

    @JsonProperty(required = true)
    private String issuedAuthority;

    private List<String> allowedApplicantTypes;

    @JsonProperty(required = true)
    private boolean questionaryRequired;

    private boolean active = false;
}
