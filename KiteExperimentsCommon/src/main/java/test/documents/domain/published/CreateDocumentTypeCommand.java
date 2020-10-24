package test.documents.domain.published;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import test.common.domain.published.DomainCommand;

/**
 * @author s.smitienko
 */
@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonClassDescription("Команда создания нового типа документов")
@ToString
public class CreateDocumentTypeCommand implements DomainCommand {

    @JsonProperty(required = true)
    private String code;

    private String classifierCode;

    @JsonProperty(required = true)
    private String name;

    private String description;

    @JsonProperty(required = true)
    private String attachmentPolicy;
}
