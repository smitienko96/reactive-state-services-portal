package test.documents.domain.published;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter @Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PublishedServiceConditionsSelection {

    @JsonProperty(required = true)
    private String serviceId;

    private List<PublishedConditionSelection> conditions;
}
