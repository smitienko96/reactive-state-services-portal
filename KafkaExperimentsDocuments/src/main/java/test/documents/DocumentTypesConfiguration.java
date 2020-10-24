package test.documents;

import brave.sampler.Sampler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.stream.annotation.StreamRetryTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.policy.AlwaysRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import test.common.configuration.ApplicationConfiguration;
import test.common.configuration.MongoConfiguration;
import test.common.domain.AggregateNames;
import test.common.domain.DocumentId;
import test.common.domain.DocumentTypeId;
import test.common.service.AggregateIdentifierGenerationStrategy;
import test.common.service.IAggregateMementoService;
import test.common.service.MongoMementoService;
import test.common.service.UUIDIdentifierGenerationStrategy;

import java.util.UUID;

/**
 * @author s.smitienko
 */
@Configuration
public class DocumentTypesConfiguration extends ApplicationConfiguration {

    @Bean("mementoMongoConfiguration")
    @ConfigurationProperties(prefix = "kite.mongo")
    public MongoConfiguration mongoConfiguration() {
        return new MongoConfiguration();
    }

    @Bean
    public IAggregateMementoService mementoService() {
        return new MongoMementoService();
    }

    @Bean
    public AggregateIdentifierGenerationStrategy<DocumentTypeId> documentTypeGenerationStrategy() {
        return new UUIDIdentifierGenerationStrategy<DocumentTypeId>() {
            @Override
            public DocumentTypeId createIdentifier(UUID uuid) {
                return new DocumentTypeId(uuid.toString());
            }

            public String aggregateName() {
                return AggregateNames.DOCUMENT_TYPES;
            }
        };
    }

    @Bean
    public AggregateIdentifierGenerationStrategy<DocumentId> documentGenerationStrategy() {
        return new UUIDIdentifierGenerationStrategy<DocumentId>() {
            @Override
            public DocumentId createIdentifier(UUID uuid) {
                return new DocumentId(uuid.toString());
            }

            public String aggregateName() {
                return AggregateNames.DOCUMENTS;
            }
        };
    }

    @StreamRetryTemplate
    public RetryTemplate retryTemplate() {
        RetryTemplate template = new RetryTemplate();
        template.setRetryPolicy(new AlwaysRetryPolicy());
        return template;
    }

    @Bean
    public Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }
}
