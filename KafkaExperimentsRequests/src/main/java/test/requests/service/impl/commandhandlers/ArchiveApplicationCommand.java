package test.requests.service.impl.commandhandlers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import test.common.domain.published.DomainCommand;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArchiveApplicationCommand implements DomainCommand {
    private String applicationNumber;
}
