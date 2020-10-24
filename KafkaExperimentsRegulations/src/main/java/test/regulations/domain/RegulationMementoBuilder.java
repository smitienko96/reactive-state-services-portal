package test.regulations.domain;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import test.common.domain.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author s.smitienko
 */
@Component
public class RegulationMementoBuilder extends AbstractMementoBuilder<Regulation> implements IAggregateMementoBuilder<Regulation> {

    private static final String APPLICATION_TYPES_DELIMITER = ",";

    public static final String NUMBER_FIELD = "number";
    public static final String DATE_FIELD = "date";
    public static final String VERSION_FIELD = "version";
    public static final String SERVICEID_FIELD = "serviceId";
    public static final String QUESTIONARY_REQUIRED_FIELD = "questionaryRequired";
    public static final String QUESTIONARY_DOCUMENT_TYPE_FIELD = "questionaryDocumentType";
    public static final String STATUS_FIELD = "status";
    public static final String NAME_FIELD = "name";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String ISSUED_AUTHORITY_FIELD = "authority";
    public static final String ENACTMENT_DATE_FIELD = "enactmentDate";
    public static final String TERMINATION_DATE_FIELD = "terminationDate";
    public static final String APPLICANT_TYPES_FIELD = "applicantTypes";
    public static final String CONDITIONS_FIELDS = "conditions";

    public RegulationMementoBuilder(EventPublisher eventPublisher) {
        super(eventPublisher);
    }

    @Override
    protected void fillBackupProperties(Regulation aggregate, Map<String,
            Object> properties) {
        properties.put(NUMBER_FIELD, aggregate.identifier().number());
        properties.put(DATE_FIELD,
                String.valueOf(aggregate.identifier().date().getTime()));
        properties.put(VERSION_FIELD, aggregate.version().version());
        properties.put(SERVICEID_FIELD, aggregate.serviceId().id());
        properties.put(QUESTIONARY_REQUIRED_FIELD, aggregate.isQuestionaryRequired());

        if (aggregate.questionary() != null) {
            properties.put(QUESTIONARY_DOCUMENT_TYPE_FIELD, aggregate.questionary().documentTypeId().id());
        }

        properties.put(STATUS_FIELD, aggregate.status().name());
        properties.put(NAME_FIELD, aggregate.metadata().name());
        properties.put(DESCRIPTION_FIELD, aggregate.metadata().description());
        properties.put(ISSUED_AUTHORITY_FIELD, aggregate.metadata().issuedAuthority());

        RegulationTerm term = aggregate.regulationTerm();
        if (term != null) {
            properties.put(ENACTMENT_DATE_FIELD,
                    convertDateToString(term.enactmentDate()));
            if (term.terminationDate() != null) {
                properties.put(TERMINATION_DATE_FIELD, convertDateToString(term.terminationDate()));
            }
        }
        StringJoiner joiner = new StringJoiner(APPLICATION_TYPES_DELIMITER);
        aggregate.allowedApplicantTypes().forEach(t -> joiner.add(t.name()));
        properties.put(APPLICANT_TYPES_FIELD,
                joiner.toString());

        RegulationConditionMementoBuilder conditionMementoBuilder = new RegulationConditionMementoBuilder(aggregate, getPublisher());

        Collection<Condition> conditions = aggregate.conditions();

        if (conditions != null) {
            conditions.forEach(f -> {
                Map<String, Object> fieldProps = new HashMap<>();
                conditionMementoBuilder.fillBackupProperties(f, fieldProps);
                addToList(CONDITIONS_FIELDS, fieldProps, properties);
            });
        }
    }


    @Override
    protected Regulation restoreFromProperties(Map<String, Object> properties) {
        String number = getStringValue(NUMBER_FIELD, properties);
        long date = Long.parseLong(getStringValue(DATE_FIELD, properties));
        int version = getIntegerValue(VERSION_FIELD, properties);
        String serviceId = getStringValue(SERVICEID_FIELD, properties);

        boolean questionaryRequired = getBooleanValue(QUESTIONARY_REQUIRED_FIELD, properties);
        String questionaryDocumentTypeId = getStringValue(QUESTIONARY_DOCUMENT_TYPE_FIELD, properties);

        String status = getStringValue(STATUS_FIELD, properties);
        String name = getStringValue(NAME_FIELD, properties);
        String description = getStringValue(DESCRIPTION_FIELD, properties);
        String issuedAuthority = getStringValue(ISSUED_AUTHORITY_FIELD, properties);
        Date enactmentDate =
                convertDateFromString(getStringValue(ENACTMENT_DATE_FIELD, properties));
        Date terminationDate = convertDateFromString(getStringValue(TERMINATION_DATE_FIELD, properties));
        String[] applicationTypes =
                getStringValue(APPLICANT_TYPES_FIELD, properties).split(APPLICATION_TYPES_DELIMITER);
        List<ApplicantType> applicantTypesList =
                Stream.of(applicationTypes).map(s -> ApplicantType.valueOf(s)).collect(Collectors.toList());

        ApplicantQuestionnary questionary = questionaryDocumentTypeId != null ?
                new ApplicantQuestionnary(new DocumentTypeId(questionaryDocumentTypeId)) : null;

        RegulationTerm term = enactmentDate != null ? new RegulationTerm(enactmentDate, terminationDate) : null;

        Regulation regulation = new Regulation(getPublisher(), new RegulationIdentifier(number, new Date(date)),
                new AggregateVersion(version), new ServiceId(serviceId),
                new RegulationMetadata(name, description, issuedAuthority),
                applicantTypesList, questionaryRequired,
                questionary,
                term,
                Regulation.Status.valueOf(status));

        RegulationConditionMementoBuilder conditionMementoBuilder =
                new RegulationConditionMementoBuilder(regulation, getPublisher());

        List<Map<String, Object>> conditions = getListOfValues(CONDITIONS_FIELDS,
                properties);

        if (!CollectionUtils.isEmpty(conditions)) {
            conditions.forEach(f -> {
                Condition condition = conditionMementoBuilder.restoreFromProperties(f);
                regulation.addCondition(condition);
            });
        }

        return regulation;
    }


    @Override
    public String getIdentifierKey() {
        return NUMBER_FIELD;
    }

    @Override
    public String getVersionKey() {
        return VERSION_FIELD;
    }


}
