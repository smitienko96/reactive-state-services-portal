package test.regulations;

import brave.sampler.Sampler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.stream.annotation.StreamRetryTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.policy.AlwaysRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import test.common.configuration.ApplicationConfiguration;
import test.common.configuration.MongoConfiguration;
import test.common.service.IAggregateMementoService;
import test.common.service.MongoMementoService;

@Configuration
public class RegulationsConfiguration extends ApplicationConfiguration {

    @Bean("mementoMongoConfiguration")
    @ConfigurationProperties(prefix = "kite.mongo")
    public MongoConfiguration mongoConfiguration() {
        return new MongoConfiguration();
    }

    @Bean
    public IAggregateMementoService mementoService() {
        return new MongoMementoService();
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
