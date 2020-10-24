package test.requests.domain.published;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonClassDescription("Статус заявления")
@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
public class PublishedApplicationStatus {

    @JsonPropertyDescription("Код статуса")
    private String code;

    @JsonPropertyDescription("Наименование статуса")
    private String name;
}
