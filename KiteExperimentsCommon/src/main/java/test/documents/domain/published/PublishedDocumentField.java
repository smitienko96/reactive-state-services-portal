package test.documents.domain.published;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.*;

import java.util.List;

/**
 * @author s.smitienko
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"documentTypeId", "code"})
@JsonClassDescription("Поле документа")
public class PublishedDocumentField {

    @JsonProperty(required = true)
    @NonNull
    @JsonPropertyDescription("Идентификатор типа документа")
    private String documentTypeId;

    @JsonProperty(required = true)
    @NonNull
    @JsonPropertyDescription("Код поля документа")
    private String code;

    @JsonPropertyDescription("Код поля документа в системе классификации")
    private String classifierCode;

    @JsonProperty(required = true)
    @NonNull
    @JsonPropertyDescription("Наименование поля документа")
    private String name;

    @JsonPropertyDescription("Описание поля документа")
    private String description;

    @JsonProperty(required = true)
    @NonNull
    @JsonPropertyDescription("Тип данных поля документа")
    private String dataType;

    @JsonPropertyDescription("Возможные значения поля документа")
    private List<String> options;
}
