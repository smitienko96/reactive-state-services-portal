package test.requests.domain.published;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author s.smitienko
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
@JsonClassDescription("Метаданные поля документа")
public class PublishedFieldMetadata extends PublishedApplicationMetadata {

    @JsonPropertyDescription("Тип данных поля")
    @JsonProperty(required = true)
    private DocumentFieldType fieldType;

    @JsonPropertyDescription("Список возможных значений поля для типа " +
            "данных OPTIONS")
    @JsonView(ViewTypes.Prepare.class)
    private List<String> fieldOptions;
}
