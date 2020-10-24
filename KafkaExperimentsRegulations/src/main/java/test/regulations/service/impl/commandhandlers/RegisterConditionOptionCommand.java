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

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonClassDescription("Команда регистрации опции условия")
public class RegisterConditionOptionCommand implements DomainCommand {

    @JsonProperty(required = true)
    private String regulationNumber;

    @JsonFormat(pattern = "dd-MM-yyyy", shape = JsonFormat.Shape.STRING)
    @JsonProperty(required = true)
    private Date regulationDate;

    @JsonProperty(required = true)
    private String conditionCode;

    @JsonProperty(required = true)
    private String optionCode;

    private int order;

    @JsonProperty(required = true)
    private String value;

    private String description;

    private List<String> typeCodes;

}
