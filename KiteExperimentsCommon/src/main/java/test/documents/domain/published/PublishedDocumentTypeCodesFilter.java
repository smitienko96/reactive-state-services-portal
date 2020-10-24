package test.documents.domain.published;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@JsonClassDescription("Фильтр кодов документов")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PublishedDocumentTypeCodesFilter {

    @JsonPropertyDescription("Список кодов документов")
    private List<PublishedDocumentTypeCode> documentTypeCodes;
}
