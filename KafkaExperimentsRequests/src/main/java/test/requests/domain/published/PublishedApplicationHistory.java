package test.requests.domain.published;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author s.smitienko
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@JsonClassDescription("Заявление на получение услуги заявителем " +
        "(представление для просмотра истории)")
public class PublishedApplicationHistory {

    @JsonProperty(required = true)
    @JsonPropertyDescription("Номер заявления")
    private String appicationNumber;

    @JsonProperty(required = true)
    @JsonPropertyDescription("Состояние заявления")
    private String applicationStatus;

    @JsonProperty(required = true)
    @JsonPropertyDescription("Идентификатор заявителя")
    private String applicantId;

    @JsonProperty(required = true)
    @JsonPropertyDescription("Идентификатор последнего оператора, изменившего заявление")
    private String lastOperatorId;

    @JsonProperty(required = true)
    @JsonPropertyDescription("Дата последнего изменения заявления")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date lastChangedDate;


    @JsonPropertyDescription("История работы с заявлением")
    @JsonProperty(required = true)
    private List<PublishedApplicationHistoryEntry> historyEntries = new ArrayList<>();

    public void addEntry(PublishedApplicationHistoryEntry entry) {
        historyEntries.add(entry);
    }
}
