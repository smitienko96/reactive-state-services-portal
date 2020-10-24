package test.documents.service.commandhandlers;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import test.common.domain.published.DomainCommand;

import java.util.Set;

/**
 * @author s.smitienko
 */
@JsonClassDescription("Команда регистрации поля документа")
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterDocumentFieldCommand implements DomainCommand {

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

    private Set<String> options;

    @JsonPropertyDescription("Является ли поле обязательным")
    private boolean required = false;
}
