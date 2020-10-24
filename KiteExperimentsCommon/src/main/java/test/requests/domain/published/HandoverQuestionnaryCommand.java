package test.requests.domain.published;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import test.common.domain.published.DomainCommand;

/**
 * @author s.smitienko
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonClassDescription("Команда выдачи анкеты заявления заявителю")
public class HandoverQuestionnaryCommand implements DomainCommand {

    @JsonPropertyDescription("Код типа заявления")
    private String applicationType;

    @JsonPropertyDescription("Идентификатор заявителя")
    private String applicantId;

    @JsonPropertyDescription("Вид заявителя")
    private String applicantKind;
}
