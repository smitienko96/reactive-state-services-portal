package test.requests.domain.published;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author s.smitienko
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonClassDescription("Элемент истории работы с заявлением")
@Getter
@Setter
public class PublishedApplicationHistoryEntry {

    @JsonPropertyDescription("Дата изменения")
    private Date changeDate;

    @JsonPropertyDescription("Тип изменения")
    private String changeType;

    @JsonPropertyDescription("Идентификатор оператора, совершившего изменение")
    private String operatorId;

    @JsonPropertyDescription("Описание изменения")
    private String changeDescription;
}
