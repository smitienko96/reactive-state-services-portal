package test.regulations.domain.published;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonClassDescription("Вариант выбора условий предоставления")
public class PublishedRegulationConditionOption {

    @JsonProperty(required = true)
    private String code;

    @JsonProperty(required = true)
    private String value;

    private String description;

    private List<String> documentTypes;
}
