package test.requests.domain.published;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.*;

/**
 * @author s.smitienko
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonClassDescription("Заявление на получение услуги заявителем")
public class PublishedApplication {

    @JsonPropertyDescription("Номер заявления")
    @JsonProperty(required = true)
    @NonNull
    private String number;

    @JsonPropertyDescription("Состояние заявления")
    @JsonProperty(required = true)
    @NonNull
    private PublishedApplicationStatus status;

    @JsonPropertyDescription("Тип заявления")
    @JsonProperty(required = true)
    @NonNull
    private String applicationType;

    @JsonPropertyDescription("Идентификатор заявителя")
    @JsonProperty(required = true)
    @NonNull
    private String applicantId;

    @JsonPropertyDescription("Идентификатор оператора, зарегистрировавшего " +
            "заявление")
    @JsonProperty(required = true)
    @NonNull
    private String creatorId;

    @JsonPropertyDescription("Идентификатор оператора, изменившего" +
            "заявление последним")
    @JsonProperty(required = true)
    @NonNull
    private String updaterId;
}
