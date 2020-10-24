package test.requests.domain.published;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter @Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PublishedAddress {

    private String region;
    private String city;
    private String street;

}
