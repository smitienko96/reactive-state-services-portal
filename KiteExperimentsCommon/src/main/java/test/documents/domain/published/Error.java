package test.documents.domain.published;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author s.smitienko
 */
@JsonClassDescription("Ошибка обработки")
@Getter
@Setter
public class Error {

    @JsonPropertyDescription("Описание ошибки")
    private String description;

    @JsonPropertyDescription("Идентификатор проблемной сущности")
    private String entityIdentifier;

    @JsonPropertyDescription("Наименование проблемной сущности")
    private String entityName;

    private List<String> stackTrace;
}
