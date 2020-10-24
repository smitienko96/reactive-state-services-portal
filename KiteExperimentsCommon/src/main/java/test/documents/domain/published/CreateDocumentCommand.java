package test.documents.domain.published;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import test.common.domain.published.DomainCommand;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@JsonClassDescription("Команда создания нового документа")
@ToString
public class CreateDocumentCommand implements DomainCommand {

    @JsonProperty(required = true)
    private String documentTypeId;

    @JsonProperty(required = true)
    private String name;

    private String description;

    private String referenceNumber;

}
