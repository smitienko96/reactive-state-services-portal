package test.requests.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import test.requests.domain.IPublishedApplicationHistoryRepository;
import test.requests.domain.IPublishedApplicationsRepository;
import test.requests.domain.published.PublishedApplication;
import test.requests.domain.published.PublishedApplicationHistory;

/**
 * @author s.smitienko
 */
@RestController
@RequestMapping("/applications")
public class ApplicationsController {

    @Autowired
    private IPublishedApplicationsRepository applicationRepository;

    @Autowired
    private IPublishedApplicationHistoryRepository historyRepository;

    /**
     *
     */
    @GetMapping("/byApplicant/{applicantId}")
    public Flux<PublishedApplication> searchByApplicant(@PathVariable String applicantId) {
        return applicationRepository.searchByApplicant(applicantId);

    }

    @GetMapping("/byOperator/{operatorId}")
    public Flux<PublishedApplication> searchByOperator(@PathVariable String operatorId) {
        return applicationRepository.searchByOperator(operatorId);
    }

    @GetMapping("/history/{applicationNumber}")
    public Mono<PublishedApplicationHistory> getApplicationHistory(@PathVariable String applicationNumber) {
        return historyRepository.getApplicationHistory(applicationNumber);
    }


}
