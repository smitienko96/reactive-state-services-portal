package test.requests.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import test.common.domain.DocumentTypeCode;
import test.common.service.CircuitBreakerHelper;
import test.documents.domain.published.PublishedConditionSelection;
import test.documents.domain.published.PublishedDocumentTypeCode;
import test.documents.domain.published.PublishedServiceConditionsSelection;
import test.regulations.domain.published.PublishedRegulation;
import test.requests.domain.ApplicantId;
import test.requests.domain.ApplicationType;
import test.requests.domain.DocumentFieldCode;
import test.requests.domain.published.PublishedApplicantQuestionnary;
import test.requests.service.IDocumentsService;
import test.requests.service.IRegulationsService;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RestRegulationsService implements IRegulationsService {

    @Autowired
    private WebClient.Builder builder;

    @Autowired
    private IDocumentsService documentsService;

    @Value("${kite.rest.regulations.service.name:regulations-service}")
    private String regulationsServiceName;

    @Override
    public Flux<DocumentFieldCode> getApplicantQuestionaryFields(ApplicationType applicationType) {
        String serviceId = getServiceIdForApplicationType(applicationType);

        Flux<DocumentFieldCode> result = buildWebClient().get().uri("/regulations/{id}", serviceId)
                .retrieve().bodyToMono(PublishedRegulation.class)
                .flatMapMany(r -> getDocumentFieldsForQuestionary(r.getQuestionaryDocumentType()));

        return CircuitBreakerHelper.wrapWithHystrix(result, "getApplicantQuestionaryFields",
                Flux.error(new RuntimeException("Unable to fetch applicant questionary fields")));
    }

    @Override
    public Flux<DocumentTypeCode> getApplicantDocumentTypes(ApplicantId applicantId, ApplicationType applicationType,
                                                            PublishedApplicantQuestionnary questionary) {
        Flux<DocumentTypeCode> result = buildWebClient().post().uri("/regulations/applicant-documents")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(buildServiceConditions(applicationType, questionary)))
                .retrieve()
                .bodyToFlux(PublishedDocumentTypeCode.class).map(code -> new DocumentTypeCode(code.getCode()));

        return CircuitBreakerHelper.wrapWithHystrix(result, "getApplicantDocumentTypes", getDefaultApplicantDocumentTypes(applicantId, applicationType));
    }


    private Flux<DocumentTypeCode> getDefaultApplicantDocumentTypes(ApplicantId applicantId, ApplicationType applicationType) {
        return Flux.create(emitter -> {
            String errorText = MessageFormat.format("Error occurred while fetching document " +
                    "types for applicant [{0}] and application type [{1}]", applicantId.id(), applicationType.typeCode());
            log.error(errorText);
            emitter.error(new RuntimeException(errorText));
        });
    }

    private Flux<DocumentFieldCode> getDocumentFieldsForQuestionary(String questionaryDocType) {
        if (StringUtils.isEmpty(questionaryDocType)) {
            return Flux.empty();
        }
        return documentsService.getFieldCodesForDocumentType(new DocumentTypeCode(questionaryDocType));
    }

    private static String getServiceIdForApplicationType(ApplicationType type) {
        return type.typeCode();
    }

    private static PublishedServiceConditionsSelection buildServiceConditions(ApplicationType applicationType, PublishedApplicantQuestionnary questionnary) {
        PublishedServiceConditionsSelection conditions = new PublishedServiceConditionsSelection();

        conditions.setServiceId(getServiceIdForApplicationType(applicationType));

        List<PublishedConditionSelection> selections = questionnary.getFields().stream().filter(f -> f.getFieldValue() != null)
                .map(f -> new PublishedConditionSelection(f.getFieldCode(), f.getFieldValue()))
                .collect(Collectors.toList());
        conditions.setConditions(selections);

        return conditions;
    }

    private WebClient buildWebClient() {
        return builder.baseUrl("http://" + regulationsServiceName).build();
    }
}
