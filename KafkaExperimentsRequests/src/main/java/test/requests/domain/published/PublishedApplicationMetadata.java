package test.requests.domain.published;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Getter;
import lombok.Setter;

/**
 * @author s.smitienko
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@JsonClassDescription("Метаданные элемента заявления")
public class PublishedApplicationMetadata {

    @JsonPropertyDescription("Код элемента")
    @JsonProperty(required = true)
    private String code;

    @JsonPropertyDescription("Наименование элемента")
    @JsonProperty(required = true)
    private String name;

    @JsonPropertyDescription("Описание элемента")
    private String description;
}
