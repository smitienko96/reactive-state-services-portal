package test.requests.service.impl.commandhandlers;


import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import test.common.domain.published.DomainCommand;

@Setter
@Getter
@NoArgsConstructor
@JsonClassDescription("Команда регистрации физического лица")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterIndividualCommand implements DomainCommand {

    @JsonProperty(required = true)
    private String snils;

    @JsonProperty(required = true)
    private String firstName;

    @JsonProperty(required = true)
    private String lastName;

    private String patronymic;
}
