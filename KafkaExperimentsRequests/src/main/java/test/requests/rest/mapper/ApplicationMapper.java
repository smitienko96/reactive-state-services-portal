package test.requests.rest.mapper;

import test.requests.domain.Application;
import test.requests.domain.published.PublishedApplication;
import test.requests.domain.published.PublishedApplicationStatus;

/**
 * @author s.smitienko
 */
public class ApplicationMapper {
    private ApplicationMapper() {}

    /**
     *
     * @param application
     * @return
     */
    public static PublishedApplication toPublished(Application application) {
        return new PublishedApplication(application.applicationNumber().id(),
                new PublishedApplicationStatus(application.status().name(), application.status().readable()), application.applicationType().typeCode(),
                application.applicant().id(), application.operator().id(),
                application.operator().id());
    }
}
