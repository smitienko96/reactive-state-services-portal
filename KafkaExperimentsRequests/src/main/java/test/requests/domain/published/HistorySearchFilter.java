package test.requests.domain.published;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author s.smitienko
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonClassDescription("Фильтр поиска истории работы с заявлением")
public class HistorySearchFilter {

    @JsonPropertyDescription("Идентификатор заявителя")
    private String applicantId;

    @JsonPropertyDescription("Идентификатор оператора, создавшего или " +
            "отредактировавшего обращение")
    private String operatorId;

    @JsonPropertyDescription("Начало интервала поиска")
    @JsonFormat(pattern = "dd-MM-yyyy", shape = JsonFormat.Shape.STRING)
    private Date beginDate;

    @JsonPropertyDescription("Конец интервала поиска")
    @JsonFormat(pattern = "dd-MM-yyyy", shape = JsonFormat.Shape.STRING)
    private Date endDate;
}
