package test.requests.domain;

import org.apache.commons.lang3.RandomStringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import test.common.domain.AggregateNames;
import test.common.domain.BuilderResult;
import test.common.domain.DocumentTypeCode;
import test.common.domain.EventPublisher;
import test.common.service.AggregateIdentifierGenerator;
import test.requests.domain.published.PublishedApplicantQuestionnary;
import test.requests.domain.published.PublishedQuestionaryField;
import test.requests.service.IDocumentsService;
import test.requests.service.IOperatorService;
import test.requests.service.IRegulationsService;

import java.util.ArrayList;
import java.util.List;

public class ApplicantDocumentService {

    private static List<String> FIELDS_TO_PREFILL;

    static {
        FIELDS_TO_PREFILL = new ArrayList<>();
        FIELDS_TO_PREFILL.add("APPLICANT.FIO");
        FIELDS_TO_PREFILL.add("APPLICANT.PASSPORT.NO");
        FIELDS_TO_PREFILL.add("APPLICANT.PASSPORT.ISSUE.DATE");
        FIELDS_TO_PREFILL.add("APPLICANT.PASSPORT.ISSUE.AUTHORITY");
    }

    private EventPublisher publisher;

    private IApplicationsRepository applicationsRepository;

    private IDocumentsService documentsService;
    private IRegulationsService regulationsService;
    private IOperatorService operatorService;
    private AggregateIdentifierGenerator identifierGenerator;

    public ApplicantDocumentService(EventPublisher publisher, IApplicationsRepository applicationsRepository,
                                    IDocumentsService documentsService,
                                    IRegulationsService regulationsService,
                                    IOperatorService operatorService, AggregateIdentifierGenerator identifierGenerator) {
        this.publisher = publisher;
        this.applicationsRepository = applicationsRepository;
        this.documentsService = documentsService;
        this.regulationsService = regulationsService;
        this.operatorService = operatorService;
        this.identifierGenerator = identifierGenerator;
    }

    /**
     * Выдает анкету заявления заявителю
     *
     * @param applicant
     * @param applicationType
     * @return
     */
    public Mono<PublishedApplicantQuestionnary> handoverQuestionary(ApplicantId applicant,
                                                                    ApplicationType applicationType) {
        Flux<DocumentFieldCode> questionaryFields = regulationsService.getApplicantQuestionaryFields(applicationType);
        Flux<PublishedQuestionaryField> questionaryPrefilled = prefillQuestionaryFields(questionaryFields, applicant);
        return questionaryPrefilled.collectList().map(fields -> new PublishedApplicantQuestionnary(applicationType.typeCode(), fields));
    }

    /**
     * @param applicant
     * @param questionary
     * @return
     */
    public Mono<Application> handoverDraft(ApplicantId applicant, PublishedApplicantQuestionnary questionary) {
        ApplicationType applicationType = new ApplicationType(questionary.getApplicationType());
        Flux<DocumentTypeCode> typeCodes = regulationsService.getApplicantDocumentTypes(applicant, applicationType, questionary);

        Flux<ApplicantDocumentType> documentTypes = typeCodes.collectList().flatMapMany(l -> documentsService.getDocumentTypesForCodes(l));

        Flux<DocumentFieldCode> fieldCodesAll = documentTypes
                .flatMap(t -> documentsService.getFieldCodesForDocumentType(t.typeCode()));

        Mono<List<ApplicantDocumentField>> prefilledFields = prefillDocumentFields(applicant, applicationType, fieldCodesAll);

        Mono<List<ApplicantDocumentType>> documentTypesCollected = documentTypes.collectList()
                .defaultIfEmpty(new ArrayList<>());


        OperatorId operator = operatorService.getCurrentOperator();
        ApplicationNumber number = generateNewNumber();

        return documentTypesCollected.zipWith(prefilledFields, (types, fields) -> {

            ApplicationBuilder builder = ApplicationBuilder.fresh(publisher);

            return builder.withNumber(number)
                    .withApplicationType(applicationType)
                    .withApplicant(applicant)
                    .withOperator(operator)
                    .withApplicantDocumentTypes(types)
                    .withApplicantDocumentField(fields);

        }).map(builder -> builder.build())
                .flatMap(this::handleBuilderResult);
    }

    private Mono<Application> handleBuilderResult(BuilderResult<Application> result) {
        Application application = result.getAggregate();

        if (!result.isSaveRequired()) {
            return Mono.just(application);
        }

        return applicationsRepository.save(application).handle((success, sink) -> {
            if (!success) {
                sink.error(new RuntimeException("Error while saving new application draft"));
            }
            sink.next(application);
            sink.complete();
        });
    }

    private Mono<List<ApplicantDocumentField>> prefillDocumentFields(ApplicantId applicantId, ApplicationType applicationType, Flux<DocumentFieldCode> fieldCodes) {
        return fieldCodes.filter(c -> FIELDS_TO_PREFILL.contains(c.code()))
                .map(c -> new ApplicantDocumentField(c, RandomStringUtils.random(10))).collectList()
                .defaultIfEmpty(new ArrayList<>());
    }


    private Flux<PublishedQuestionaryField> prefillQuestionaryFields(Flux<DocumentFieldCode> questionaryFields, ApplicantId applicantId) {
        return questionaryFields.map(c -> new PublishedQuestionaryField(c.flatCode(), null));
    }

    private ApplicationNumber generateNewNumber() {
        return identifierGenerator.generate(AggregateNames.APPLICATIONS);
    }
}
