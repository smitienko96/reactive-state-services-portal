package test.common.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author s.smitienko
 */
@Configuration
@Getter @Setter
@Validated
public class MongoConfiguration {

    @NotEmpty
    private String dbAddress;

    @NotNull
    private Database database;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String authenticationDb;

    @Getter @Setter
    public static class Database {
        @NotEmpty
        private String name;
    }
}
