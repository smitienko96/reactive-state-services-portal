package test.requests.domain.published;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author s.smitienko
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonClassDescription("Поле документа заявления")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class PublishedApplicationField {

    @JsonPropertyDescription("Метаданные поля документа")
    private PublishedFieldMetadata fieldMetadata;

    @JsonPropertyDescription("Значение поля документа")
    @JsonView(value = {ViewTypes.Show.class, ViewTypes.Edit.class})
    private String fieldValue;
}
