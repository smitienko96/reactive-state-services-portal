package test.documents;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.config.EnableWebFlux;
import test.common.configuration.MongoConfiguration;
import test.common.service.KafkaEventProcessor;

/**
 * @author s.smitienko
 */
@SpringBootApplication
@EnableWebFlux
@EnableDiscoveryClient
@EnableBinding(KafkaEventProcessor.class)
@Import({MongoConfiguration.class,
        DocumentTypesConfiguration.class})
@EnableCircuitBreaker
@ComponentScan(basePackages = {"test.documents", "test.documents.rest", "test.common.rest"})
//@ComponentScan
@Slf4j
public class DocumentTypesApplication {


    public static void main(String[] args) {
        SpringApplication app =
                new SpringApplication(DocumentTypesApplication.class);
        app.setWebApplicationType(WebApplicationType.REACTIVE);
        app.run(args);
    }
}
