package test.requests.domain.published;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonClassDescription("Заявитель - юридическое лицо")
public class PublishedOrganization {

    @JsonProperty(required = true)
    private String inn;

    @JsonProperty(required = true)
    private String shortName;

    private String longName;

    private PublishedAddress actualAddress;

    private PublishedAddress legalAddress;
}
