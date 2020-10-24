package test.requests;

import brave.sampler.Sampler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.stream.annotation.StreamRetryTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.policy.AlwaysRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import test.common.configuration.ApplicationConfiguration;
import test.common.domain.EventPublisher;
import test.requests.domain.ApplicantDocumentService;
import test.requests.domain.IApplicationsRepository;
import test.requests.service.IDocumentsService;
import test.requests.service.IOperatorService;
import test.requests.service.IRegulationsService;
import test.requests.service.impl.RestDocumentsService;
import test.requests.service.impl.RestRegulationsService;
import test.requests.service.impl.SingletonOperatorService;

@Configuration
public class ApplicationsConfiguration extends ApplicationConfiguration {

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private IApplicationsRepository applicationsRepository;

    @Bean
    public ApplicantDocumentService applicantDocumentService() {
        return new ApplicantDocumentService(eventPublisher, applicationsRepository,
                documentsService(), regulationsService(), operatorService(), identifierGenerator());
    }

    @Bean
    public IDocumentsService documentsService() {
        return new RestDocumentsService();
    }

    @Bean
    public IRegulationsService regulationsService() {
        return new RestRegulationsService();
    }

    @Bean
    public IOperatorService operatorService() {
        return new SingletonOperatorService();
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
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
