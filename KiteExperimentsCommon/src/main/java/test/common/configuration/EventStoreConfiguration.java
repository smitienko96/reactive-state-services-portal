package test.common.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.schema.client.ConfluentSchemaRegistryClient;
import org.springframework.cloud.stream.schema.client.SchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import test.common.domain.EventPublisher;
import test.common.service.*;

/**
 * @author s.smitienko
 */
@Configuration
@EnableBinding(KafkaEventProcessor.class)
public class EventStoreConfiguration {

    @Bean
    public EventPublisher eventPublisher() {
        return new EventStoreEventPublisher();
    }

    @Bean
    public IEventStore eventStore() {
        return new EventStoreKafka();
    }

    @Bean
    public EventsHolderQueryServiceKafka holderQueryServiceKafka() {
        return new EventsHolderQueryServiceKafka();
    }

    @Bean("eventStoreMongoConfiguration")
    @ConfigurationProperties(prefix = "kite.eventstore.mongo")
    public MongoConfiguration eventStoreMongoConfig() {
        return new MongoConfiguration();
    }

    @Bean
    public IEventsHolderRemoteAccessor eventsHolderRemoteAccessor() {
        return new EventsHolderMongoService();
    }

    @Bean
    public KafkaStoreConfigurationProperties kafkaConfig() {
        return new KafkaStoreConfigurationProperties();
    }

    @Bean
    public EventConversionService eventConversionService() {
        return new EventConversionService();
    }

    @Bean
    public SchemaRegistryClient schemaRegistryClient(@Value("${spring.cloud.stream.schemaRegistryClient.endpoint}") String endpoint) {
        ConfluentSchemaRegistryClient client = new ConfluentSchemaRegistryClient();
        client.setEndpoint(endpoint);
        return client;
    }
}
