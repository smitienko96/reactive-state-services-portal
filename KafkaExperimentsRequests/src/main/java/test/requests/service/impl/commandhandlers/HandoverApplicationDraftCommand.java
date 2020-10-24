package test.requests.service.impl.commandhandlers;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Getter;
import lombok.Setter;
import test.common.domain.published.DomainCommand;
import test.requests.domain.published.PublishedApplicantQuestionnary;

/**
 * @author s.smitienko
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonClassDescription("Команда выдачи бланка заявления заявителю")
public class HandoverApplicationDraftCommand implements DomainCommand {

    @JsonPropertyDescription("Идентификатор заявителя")
    @JsonProperty(required = true)
    private String applicantId;

    @JsonPropertyDescription("Анкета заявителя")
    @JsonProperty(required = true)
    private PublishedApplicantQuestionnary questionnary;
}
