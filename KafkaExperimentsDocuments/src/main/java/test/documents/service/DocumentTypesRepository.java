package test.documents.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import test.common.domain.AggregateNames;
import test.common.domain.DocumentTypeId;
import test.common.service.MementoBasedAggregateRepository;
import test.documents.domain.DocumentType;
import test.documents.domain.DocumentTypeCode;
import test.documents.domain.DocumentTypeMementoBuilder;
import test.documents.domain.IDocumentTypesRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author s.smitienko
 */
@Component
public class DocumentTypesRepository extends MementoBasedAggregateRepository<DocumentType, DocumentTypeId> implements IDocumentTypesRepository {

    @Override
    public Mono<DocumentType> getByCode(DocumentTypeCode code) {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put(DocumentTypeMementoBuilder.CODE_FIELD, code.code());
        if (!StringUtils.isBlank(code.classifierCode())) {
            filterMap.put(DocumentTypeMementoBuilder.CLASSIFIER_CODE_FIELD,
                    code.classifierCode());
        }

        return checkAndReturnUnique(code.toString(), getByFilter(filterMap), false);
    }

    @Override
    public Flux<DocumentType> getAll() {
        return getByFilter(Collections.emptyMap());
    }

    @Override
    protected String aggregateName() {
        return AggregateNames.DOCUMENT_TYPES;
    }
}
