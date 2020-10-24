package test.requests.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import test.requests.domain.published.PublishedApplication;
import test.requests.domain.published.PublishedApplicationView;

/**
 * @author s.smitienko
 */
public interface IPublishedApplicationsRepository {

    Flux<PublishedApplication> searchByApplicant(String applicantId);

    Flux<PublishedApplication> searchByOperator(String operatorId);

    Mono<PublishedApplicationView> getPublishedApplicationView(String applicationNumber);
}
