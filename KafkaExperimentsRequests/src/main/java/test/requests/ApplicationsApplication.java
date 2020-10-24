package test.requests;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.config.EnableWebFlux;
import test.common.service.KafkaEventProcessor;

/**
 * @author s.smitienko
 */
@SpringBootApplication
@EnableWebFlux
@EnableDiscoveryClient
@EnableBinding(KafkaEventProcessor.class)
@Import({ApplicationsConfiguration.class,
        PersistenceConfiguration.class})
@EnableCircuitBreaker
@ComponentScan(basePackages = {"test.requests", "test.requests.rest"})
@Slf4j
public class ApplicationsApplication {

    public static void main(String[] args) {
        SpringApplication app =
                new SpringApplication(ApplicationsApplication.class);
        app.setWebApplicationType(WebApplicationType.REACTIVE);
        app.run(args);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http) {
        return http.authorizeExchange()
                .anyExchange()
                .permitAll()
                .and().csrf().disable().build();
    }
}
