package test.regulations.domain;

import org.springframework.util.CollectionUtils;
import test.common.domain.DocumentTypeCode;

import java.util.*;
import java.util.stream.Collectors;

public class ConditionOptions {

    private Map<ConditionOption, List<DocumentTypeCode>> documentTypeCodes;

    public ConditionOptions() {
        this.documentTypeCodes = new TreeMap<>();
    }

    /**
     *
     * @param option
     * @param typeCode
     * @return
     */
    public boolean addOption(ConditionOption option, DocumentTypeCode typeCode) {
        List<DocumentTypeCode> typeCodes = documentTypeCodes.get(option);
        if (typeCodes == null) {
            typeCodes = new ArrayList<>();
            documentTypeCodes.put(option, typeCodes);
        }

        if (typeCodes.contains(typeCode)) {
            return false;
        }
        return typeCodes.add(typeCode);
    }

    /**
     *
     * @param option
     * @param typeCodes
     */
    public void addOption(ConditionOption option,
                             List<DocumentTypeCode> typeCodes) {
        typeCodes.forEach(code -> addOption(option, code));
    }

    /**
     * @param optionCode
     * @return
     */
    public List<DocumentTypeCode> documentTypesForCode(ConditionOptionCode optionCode) {
        return documentTypeCodes.entrySet().stream()
                .filter(e -> e.getKey().code().equals(optionCode))
                .flatMap(e -> e.getValue().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<DocumentTypeCode> allPossibleDocumentTypes() {
        return documentTypeCodes.values().stream().flatMap(l -> l.stream()).distinct().collect(Collectors.toList());
    }

    public Set<ConditionOption> optionsSet() {
        return documentTypeCodes.keySet();
    }

    public boolean isComplete() { return documentTypeCodes.entrySet().stream().anyMatch(e -> !CollectionUtils.isEmpty(e.getValue())); }
}
