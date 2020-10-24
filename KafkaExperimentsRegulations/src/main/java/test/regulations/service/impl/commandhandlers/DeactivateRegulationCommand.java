package test.regulations.service.impl.commandhandlers;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import test.common.domain.published.DomainCommand;

import java.util.Date;

@Getter
@Setter
@JsonClassDescription("Команда деактивации регламента")
public class DeactivateRegulationCommand implements DomainCommand {

    private String regulationNumber;

    @JsonFormat(pattern = "dd-MM-yyyy", shape = JsonFormat.Shape.STRING)
    private Date regulationDate;

    private String serviceId;

    @JsonFormat(pattern = "dd-MM-yyyy", shape = JsonFormat.Shape.STRING)
    private Date terminationDate;
}
