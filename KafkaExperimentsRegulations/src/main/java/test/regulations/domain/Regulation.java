package test.regulations.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import test.common.domain.*;
import test.regulations.domain.events.*;
import test.regulations.service.impl.commandhandlers.RegisterConditionOptionCommand;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AggregateMetadata(name = AggregateNames.REGULATIONS)
public class Regulation extends DomainAggregate<RegulationIdentifier> {

    private ServiceId serviceId;

    public enum Status {
        DRAFT, ACTIVE, INACTIVE
    }

    private RegulationMetadata metadata;
    private RegulationTerm regulationTerm;

    private List<ApplicantType> allowedApplicantTypes;
    private Map<ConditionCode, Condition> conditionsMap;

    private boolean questionaryRequired;
    private ApplicantQuestionnary questionnary;

    private Status status;

    public Regulation(EventPublisher publisher, RegulationIdentifier identifier, boolean active,
                      boolean questionaryRequired,
                      ServiceId serviceId, RegulationMetadata metadata,
                      List<ApplicantType> allowedApplicantTypes) {
        super(publisher, identifier);
        this.status = active ? Status.ACTIVE : Status.DRAFT;
        this.questionaryRequired = questionaryRequired;
        this.serviceId = serviceId;
        this.metadata = metadata;
        this.allowedApplicantTypes = allowedApplicantTypes;
        this.conditionsMap = new HashMap<>();

        done();
    }

    public Regulation(EventPublisher publisher, RegulationIdentifier identifier,
                      AggregateVersion version, ServiceId serviceId,
                      RegulationMetadata metadata, List<ApplicantType> allowedApplicantTypes,
                      boolean questionaryRequired, ApplicantQuestionnary questionnary,
                      RegulationTerm regulationTerm,
                      Status status) {
        super(publisher, identifier, version);
        this.serviceId = serviceId;
        this.metadata = metadata;
        this.allowedApplicantTypes = allowedApplicantTypes;

        this.conditionsMap = new HashMap<>();

        this.questionaryRequired = questionaryRequired;
        this.questionnary = questionnary;
        this.regulationTerm = regulationTerm;
        this.status = status;
    }

    /**
     * @param order
     * @param code
     * @param definition
     */
    public void registerCondition(int order, ConditionCode code, ConditionDefinition definition) {
        raiseEvent(new RegulationConditionAddedEvent(code, definition, order));
    }

    /**
     * @param command
     */
    public void registerConditionOption(RegisterConditionOptionCommand command) {
        List<DocumentTypeCode> typeCodes = command.getTypeCodes() != null ? command.getTypeCodes().stream()
                .map(c -> new DocumentTypeCode(c)).collect(Collectors.toList()) : null;
        raiseEvent(new RegulationConditionOptionAddedEvent(new ConditionCode(command.getConditionCode()),
                new ConditionOptionCode(command.getOptionCode()), command.getOrder(), command.getValue(), command.getDescription(), typeCodes));
    }

    /**
     * Активирует регламент услуги
     *
     * @param enactmentDate
     */
    public void activateRegulation(Date enactmentDate) {
        if (status == Status.ACTIVE) {
            log.info("Regulation {} is already active", identifier());
            return;
        }
        if (!checkActivationAllowed()) {
            log.info("Activation of regulation {} is not possible because it is not yet filled");
            return;
        }

        raiseEvent(new RegulationStatusChangedEvent(Status.ACTIVE, enactmentDate));
    }


    private boolean checkActivationAllowed() {
        return !CollectionUtils.isEmpty(allowedApplicantTypes)
                && metadata != null
                && (anyConditionComplete() && questionnary != null
                || !questionaryRequired);
    }

    private boolean anyConditionComplete() {
        return conditionsMap.values().stream().anyMatch(c -> c.isComplete());
    }

    /**
     * Деактивирует регламент услуги
     */
    public void deactivateRegulation() {
        if (status == Status.INACTIVE) {
            log.info("Regulation {} is already inactive", identifier());
            return;
        }

        raiseEvent(new RegulationStatusChangedEvent(Status.INACTIVE));
    }

    /**
     * @param documentTypeId
     */
    public void registerQuestionnary(DocumentTypeId documentTypeId) {
        if (status == Status.ACTIVE) {
            throw new RuntimeException("Unable to register questionnary for active regulation. Deactivate to continue");
        }

        raiseEvent(new QuestionaryRegisteredEvent(documentTypeId));
    }


    public void on(RegulationConditionAddedEvent conditionAdded) {
        ConditionCode code = conditionAdded.conditionCode();
        Condition condition = conditionsMap.get(code);
        if (condition != null) {
            condition.changeDefinition(conditionAdded.order(), conditionAdded.definition());
        } else {
            condition = new Condition(this, conditionAdded.order(), conditionAdded.conditionCode(),
                    conditionAdded.definition());
            conditionsMap.put(conditionAdded.conditionCode(), condition);
        }
    }

    /**
     * Добавление опции условий
     *
     * @param conditionOptionAdded
     */
    public void on(RegulationConditionOptionAddedEvent conditionOptionAdded) {
        Condition condition = ensureConditionExists(conditionOptionAdded.conditionCode());

        condition.options().addOption(
                new ConditionOption(conditionOptionAdded.order(),
                        conditionOptionAdded.code(),
                        conditionOptionAdded.value(),
                        conditionOptionAdded.description()),
                conditionOptionAdded.typeCodes());
    }

    private Condition ensureConditionExists(ConditionCode conditionCode) {
        Condition condition = conditionsMap.get(conditionCode);
        if (condition == null) {
            condition = new Condition(this, conditionCode);
            conditionsMap.put(conditionCode, condition);
        }
        return condition;
    }

    /**
     * @return
     */
    public Collection<Condition> conditions() {
        return conditionsMap.values();
    }

    void addCondition(Condition condition) {
        conditionsMap.put(condition.code(), condition);
    }

    /**
     * Изменение статуса регламента
     *
     * @param statusChangedEvent
     */
    public void on(RegulationStatusChangedEvent statusChangedEvent) {
        this.status = statusChangedEvent.status();
        if (Status.ACTIVE == this.status) {
            this.regulationTerm = new RegulationTerm(statusChangedEvent.changeDate());
        } else if (Status.INACTIVE == this.status) {
            this.regulationTerm = regulationTerm.withTermination(statusChangedEvent.changeDate());
        }
    }

    /**
     * @param questionaryRegisteredEvent
     */
    public void on(QuestionaryRegisteredEvent questionaryRegisteredEvent) {
        this.questionnary = new ApplicantQuestionnary(questionaryRegisteredEvent.documentTypeId());
    }


    /**
     * @param optionsMap
     * @return
     */
    public List<DocumentTypeCode> documentTypesForOptions(Map<ConditionCode, ConditionOptionCode> optionsMap) {
        return optionsMap.entrySet().stream()
                .filter(e -> conditionsMap.containsKey(e.getKey()))
                .flatMap(e -> conditionsMap.get(e.getKey()).options()
                        .documentTypesForCode(e.getValue()).stream())
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * @return
     */
    public List<DocumentTypeCode> allPossibleDocumentTypes() {
        return conditions().stream().filter(c -> c.isComplete())
                .flatMap(c -> c.options()
                        .allPossibleDocumentTypes().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public ServiceId serviceId() {
        return serviceId;
    }

    public boolean isQuestionaryRequired() {
        return questionaryRequired;
    }

    public Status status() {
        return status;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public RegulationMetadata metadata() {
        return metadata;
    }

    public RegulationTerm regulationTerm() {
        return regulationTerm;
    }

    public ApplicantQuestionnary questionary() {
        return questionnary;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public List<ApplicantType> allowedApplicantTypes() {
        return allowedApplicantTypes;
    }


    @Override
    public RegulationRegisteredEvent createInitialEvent() {
        return new RegulationRegisteredEvent(serviceId, allowedApplicantTypes, questionaryRequired, status);
    }

}
