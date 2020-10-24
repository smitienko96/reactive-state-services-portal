package test.requests.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import test.common.domain.DocumentTypeCode;
import test.common.domain.DocumentTypeId;
import test.common.service.CircuitBreakerHelper;
import test.documents.domain.published.PublishedDocumentField;
import test.documents.domain.published.PublishedDocumentType;
import test.documents.domain.published.PublishedDocumentTypeCode;
import test.documents.domain.published.PublishedDocumentTypeCodesFilter;
import test.requests.domain.ApplicantDocumentType;
import test.requests.domain.DocumentFieldCode;
import test.requests.service.IDocumentsService;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RestDocumentsService implements IDocumentsService {

    @Autowired
    private WebClient.Builder builder;

    @Value("${kite.rest.documents.service.name:documents-service}")
    private String documentsServiceName;

    @Override
    public Flux<ApplicantDocumentType> getDocumentTypesForCodes(List<DocumentTypeCode> typeCodes) {
        if (CollectionUtils.isEmpty(typeCodes)) {
            return Flux.empty();
        }
        Flux<ApplicantDocumentType> result = buildWebClient().post().uri(
                "document-types/byCodes")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(buildDocumentCodesFilter(typeCodes)))
                .retrieve().bodyToFlux(PublishedDocumentType.class)
                .map(t -> new ApplicantDocumentType(
                        new DocumentTypeCode(t.getCode().getCode()),
                        new DocumentTypeId(t.getDocumentTypeId())));

        return CircuitBreakerHelper.wrapWithHystrix(result,"getDocumentTypesForCodes",
                getDefaultTypesForCodes(typeCodes));

    }

    private PublishedDocumentTypeCodesFilter buildDocumentCodesFilter(List<DocumentTypeCode> typeCodes) {
        PublishedDocumentTypeCodesFilter filter = new PublishedDocumentTypeCodesFilter();
        filter.setDocumentTypeCodes(typeCodes.stream()
                .map(c -> new PublishedDocumentTypeCode(c.code()))
                .collect(Collectors.toList()));
        return filter;
    }

    private Flux<ApplicantDocumentType> getDefaultTypesForCodes(List<DocumentTypeCode> typeCodes) {
        return Flux.fromIterable(typeCodes).doOnSubscribe(
                n -> log.error("Unable to retrieve applicant document types for type codes. Falling back to default"))
                .map(c -> new ApplicantDocumentType(c, null));
    }

    @Override
    public Flux<DocumentFieldCode> getFieldCodesForDocumentType(DocumentTypeCode documentType) {
        Flux<DocumentFieldCode> result = buildWebClient().get()
                .uri("document-types/fields/{typeIdentifier}?isCode=true", documentType.code())
                .retrieve().bodyToFlux(PublishedDocumentField.class)
                .map(f -> new DocumentFieldCode(f.getCode()));

        return CircuitBreakerHelper.wrapWithHystrix(result,
                "getFieldCodesForDocumentType",
                Flux.error(new RuntimeException(String.format("Unable to fetch codes for document type [%s]", documentType.code()))));
    }


    private WebClient buildWebClient() {
        return builder.baseUrl("http://" + documentsServiceName).build();
    }
}
