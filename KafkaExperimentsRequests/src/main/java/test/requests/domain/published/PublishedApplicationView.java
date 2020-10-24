package test.requests.domain.published;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.List;

/**
 * @author s.smitienko
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
//@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonClassDescription("Заявление на получение услуги заявителем " +
        "(представление для просмотра и редактирования)")
public class PublishedApplicationView extends PublishedApplication {


    @JsonPropertyDescription("Секции заявления")
    @JsonProperty(required = true)
    @NonNull
    private List<PublishedApplicationSection> sections;

    public PublishedApplicationView(String number, PublishedApplicationStatus status, String applicationType, String applicantId, String creatorId, String updaterId) {
        super(number, status, applicationType, applicantId, creatorId, updaterId);
    }
}
