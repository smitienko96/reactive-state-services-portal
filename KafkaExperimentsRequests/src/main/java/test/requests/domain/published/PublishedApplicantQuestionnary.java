package test.requests.domain.published;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author s.smitienko
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonClassDescription("Анкета заявителя")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PublishedApplicantQuestionnary {

    @JsonPropertyDescription("Тип заявления")
    @JsonProperty(required = true)
    private String applicationType;

    @JsonPropertyDescription("Поля анкеты")
    private List<PublishedQuestionaryField> fields;
}
