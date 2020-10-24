package test.requests.domain.published.commands;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Getter;
import lombok.Setter;
import test.common.domain.published.DomainCommand;

/**
 * @author s.smitienko
 */
@JsonClassDescription("Команда получения анкеты заявителя для типа заявления")
@Getter @Setter
public class GetQuestionnaryCommand implements DomainCommand {

    @JsonPropertyDescription("Идентификатор заявителя")
    private String applicantId;

    @JsonPropertyDescription("Тип заявителя")
    private String applicantKind;

    @JsonPropertyDescription("Тип заявления")
    private String applicationType;

}
