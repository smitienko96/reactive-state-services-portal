package test.requests.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import test.requests.domain.IPublishedApplicationsRepository;
import test.requests.domain.published.PublishedApplication;
import test.requests.domain.published.PublishedApplicationStatus;
import test.requests.domain.published.PublishedApplicationView;
import test.requests.persistence.db.tables.records.ApplicationsRecord;

import static org.jooq.impl.DSL.asterisk;
import static test.requests.persistence.db.Tables.*;

/**
 * @author s.smitienko
 */
@Component
public class PublishedApplicationsJooqRepository implements IPublishedApplicationsRepository {

    @Autowired
    private DSLContext dslContext;

    @Override
    public Flux<PublishedApplication> searchByApplicant(String applicantId) {
        if (StringUtils.isBlank(applicantId)) {
            return Flux.error(new IllegalArgumentException("ApplicantId must " +
                    "be set"));
        }
        Long applicantIdLong = Long.parseLong(applicantId);

        return searchByField(APPLICATIONS_.APPLICANT_ID, applicantIdLong);
    }

    @Override
    public Flux<PublishedApplication> searchByOperator(String operatorId) {
        if (StringUtils.isBlank(operatorId)) {
            return Flux.error(new IllegalArgumentException("OperatorId must " +
                    "be set"));
        }

        return searchByField(APPLICATIONS_.CREATOR_ID, operatorId)
                .concatWith(searchByField(APPLICATIONS_.UPDATER_ID, operatorId));
    }

    private <F> Flux<PublishedApplication> searchByField(TableField<ApplicationsRecord, F> field, F fieldValue) {
        Flux<Record> records =
                Flux.fromStream(
                        dslContext.select(asterisk()).from(APPLICATIONS_)
                                .where(field.eq(fieldValue))
                                .fetchStream());

        return records.map(r -> {
            String number = r.get(APPLICATIONS_.NUMBER);
            String status = r.get(APPLICATIONS_.STATUS);
            String applicationType = r.get(APPLICATIONS_.APPLICATION_TYPE);
            String creatorId = r.get(APPLICATIONS_.CREATOR_ID);
            String updaterId = r.get(APPLICATIONS_.UPDATER_ID);
            String applicantId =
                    String.valueOf(r.get(APPLICATIONS_.APPLICANT_ID));
            return new PublishedApplication(number, new PublishedApplicationStatus(status, status), applicationType,
                    applicantId, creatorId, updaterId);
        });
    }

    @Override
    public Mono<PublishedApplicationView> getPublishedApplicationView(String applicationNumber) {
        PublishedApplicationViewRecordMapper mapper = new PublishedApplicationViewRecordMapper();
         return Flux.fromStream(dslContext.select(DSL.asterisk()).from(APPLICATIONS_)
                .innerJoin(APPLICATION_SECTIONS).on(APPLICATION_SECTIONS.APPLICATION_ID.eq(APPLICATIONS_.ID))
                .innerJoin(APPLICATION_DOCUMENT_FIELDS).on(APPLICATION_DOCUMENT_FIELDS.SECTION_ID.eq(APPLICATION_SECTIONS.ID))
                .innerJoin(INDIVIDUALS).on(INDIVIDUALS.ID.eq(APPLICATIONS_.APPLICANT_ID))
                 .where(APPLICATIONS_.NUMBER.eq(applicationNumber))
                 .fetchStream())
                 .singleOrEmpty()
                 .map(r -> mapper.map(r));
    }
}
