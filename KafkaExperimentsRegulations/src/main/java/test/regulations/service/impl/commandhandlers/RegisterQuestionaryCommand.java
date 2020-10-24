package test.regulations.service.impl.commandhandlers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import test.common.domain.published.DomainCommand;

import java.util.Date;

@Setter
@Getter
@RequiredArgsConstructor
@NoArgsConstructor
public class RegisterQuestionaryCommand implements DomainCommand {

    @JsonProperty(required = true)
    private String regulationNumber;

    @JsonProperty(required = true)
    @JsonFormat(pattern = "dd-MM-yyyy", shape = JsonFormat.Shape.STRING)
    private Date regulationDate;

    @NonNull
    @JsonProperty(required = true)
    private String documentTypeId;

    private boolean required = false;
}
