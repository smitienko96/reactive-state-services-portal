package test.requests.domain.published.commands;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import test.common.domain.published.DomainCommand;
import test.requests.domain.published.PublishedApplicantQuestionnary;

/**
 * @author s.smitienko
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonClassDescription("Команда выдачи бланка заявления на основе полей анкеты")
public class HandoverDraftCommand implements DomainCommand {

    @JsonPropertyDescription("Идентификатор заявителя")
    @JsonProperty(required = true)
    private String applicantId;

    @JsonPropertyDescription("Заполненная анкета заявителя")
    private PublishedApplicantQuestionnary questionnary;
}
