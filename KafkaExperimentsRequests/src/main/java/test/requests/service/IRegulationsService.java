package test.requests.service;

import reactor.core.publisher.Flux;
import test.common.domain.DocumentTypeCode;
import test.requests.domain.ApplicantId;
import test.requests.domain.ApplicationType;
import test.requests.domain.DocumentFieldCode;
import test.requests.domain.published.PublishedApplicantQuestionnary;

public interface IRegulationsService {
    Flux<DocumentTypeCode> getApplicantDocumentTypes(ApplicantId applicantId, ApplicationType applicationType,
                                                     PublishedApplicantQuestionnary questionary);
    Flux<DocumentFieldCode> getApplicantQuestionaryFields(ApplicationType applicationType);
}
