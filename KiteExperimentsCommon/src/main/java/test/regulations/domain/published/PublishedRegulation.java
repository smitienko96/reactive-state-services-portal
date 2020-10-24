package test.regulations.domain.published;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @author s.smitienko
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonClassDescription("Регламент предоставления услуги")
public class PublishedRegulation {

    @JsonPropertyDescription("Номер регламента")
    private String number;

    @JsonPropertyDescription("Дата принятия регламента")
    @JsonFormat(pattern = "dd-MM-yyyy", shape = JsonFormat.Shape.STRING)
    private Date date;

    @JsonPropertyDescription("Название регламента")
    private String name;

    @JsonPropertyDescription("Идентификатор услуги")
    private String serviceId;

    @JsonPropertyDescription("Тип документа анкеты заявителя")
    private String questionaryDocumentType;

    private String description;

    @JsonFormat(pattern = "dd-MM-yyyy", shape = JsonFormat.Shape.STRING)
    private Date enactmentDate;

    @JsonFormat(pattern = "dd-MM-yyyy", shape = JsonFormat.Shape.STRING)
    private Date terminationDate;

    private List<String> applicantTypes;

    private List<PublishedRegulationCondition> publishedConditions;

}
