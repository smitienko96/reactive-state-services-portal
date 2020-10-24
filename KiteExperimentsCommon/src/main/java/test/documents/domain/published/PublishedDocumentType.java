package test.documents.domain.published;

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
@AllArgsConstructor
@EqualsAndHashCode(of = "documentTypeId")
@JsonClassDescription("Тип документа")
public class PublishedDocumentType {

    @JsonProperty(required = true)
    @NonNull
    @JsonPropertyDescription("Идентификатор типа документа")
    private String documentTypeId;

    @JsonProperty(required = true)
    @JsonPropertyDescription("Код типа документа")
    @NonNull
    private PublishedDocumentTypeCode code;

    @JsonProperty(required = true)
    @JsonPropertyDescription("Наименование типа документа")
    @NonNull
    private String name;

    @JsonPropertyDescription("Описание типа документа")
    private String description;
}
