package test.documents.domain.published;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class PublishedDocumentTypeCode {

    @JsonProperty(required = true)
    @JsonPropertyDescription("Код типа документа")
    @NonNull
    private String code;

    @JsonPropertyDescription("Код типа документа в системе классификации")
    private String classifierCode;
}
