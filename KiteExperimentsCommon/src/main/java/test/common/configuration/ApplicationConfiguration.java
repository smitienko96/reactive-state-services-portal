package test.common.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import test.common.service.AggregateIdentifierGenerator;
import test.common.service.CommandExecutor;
import test.common.service.DomainEventDispatcher;
import test.common.service.ObjectMapperFactory;

/**
 * @author s.smitienko
 */

@Configuration
public abstract class ApplicationConfiguration {

    @Bean
    public CommandExecutor commandExecutor() {
        return new CommandExecutor();
    }

    @Bean
    public DomainEventDispatcher eventDispatcher() {
        return new DomainEventDispatcher();
    }

    @Bean
    public AggregateIdentifierGenerator identifierGenerator() {
        return new AggregateIdentifierGenerator();
    }

    @Bean
//    @Qualifier("webFluxMapper")
    public ObjectMapper webFluxMapper() {
        return ObjectMapperFactory.createWebFluxGenericObjectMapper();
    }

//    @Bean
//    @Qualifier("eventsStreamMapper")
//    public ObjectMapper eventsStreamMapper() {
//        return ObjectMapperFactory.createKafkaStateStoreObjectMapper();
//    }

    @Bean
    public WebFluxConfigurer webFluxConfigurer(ObjectMapper webFluxMapper) {
        return new WebFluxConfigurer() {
            @Override
            public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
                // GENERIC
                configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(webFluxMapper));
                configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(webFluxMapper));
                // EVENT STREAM
//                MimeType eventsStreamType = MimeTypeUtils.parseMimeType(EventStoreConstants.KAFKA_EVENTS_STREAM_MIME_TYPE);
//                configurer.customCodecs().encoder(new Jackson2SmileEncoder(
//                        eventsStreamMapper, eventsStreamType));
//                configurer.customCodecs().decoder(new Jackson2SmileDecoder(
//                        eventsStreamMapper, eventsStreamType));
            }
        };
    }
}
