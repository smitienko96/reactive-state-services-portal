package test.requests.domain;

import reactor.core.publisher.Flux;
import test.requests.domain.published.PublishedIndividual;
import test.requests.domain.published.PublishedOrganization;

public interface IPublishedApplicantsRepository {

    Flux<PublishedIndividual> getIndividuals();

    Flux<PublishedOrganization> getOrganizations();
}
