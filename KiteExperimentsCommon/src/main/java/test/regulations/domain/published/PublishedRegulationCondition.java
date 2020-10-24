package test.regulations.domain.published;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author s.smitienko
 */
@Getter
@Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonClassDescription("Условие предоставления услуги заявителю")
public class PublishedRegulationCondition {

    @JsonProperty(required = true)
    private String code;

    private String name;

    private String description;

    @JsonProperty(required = true)
    private List<PublishedRegulationConditionOption> options;
}
