package test.common.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * @author s.smitienko
 */
@Validated
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "kite.eventstore.kafka")
public class KafkaStoreConfigurationProperties {

    @Getter
    @Setter
    public static class LocalStore {

        private boolean enable = true;
        private String name;
    }

    @NotNull
    private LocalStore localStore;
}