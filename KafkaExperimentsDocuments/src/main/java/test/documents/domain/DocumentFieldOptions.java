package test.documents.domain;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DocumentFieldOptions {

    private Map<Integer, DocumentFieldOption> options;

    public DocumentFieldOptions() {
        this.options = new TreeMap<>();
    }

    public DocumentFieldOptions(Set<String> options) {
        List<String> optionsList = options.stream().collect(Collectors.toList());
        this.options = IntStream.range(0, options.size())
                .mapToObj(i -> new DocumentFieldOption(i, optionsList.get(i)))
                .collect(Collectors.toMap(DocumentFieldOption::order,
                        o -> o));
    }

    public boolean addOption(DocumentFieldOption option) {
        options.put(option.order(), option);
        return true;
    }

    /**
     * Проверяет входит ли указанное значение в список возможных значений опиций
     *
     * @param value
     * @return
     */
    public boolean isValueIncluded(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        return options.values().stream()
                .anyMatch(o -> o.value().equals(value.trim()));
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public Set<DocumentFieldOption> options() {
        return new HashSet<>(options.values());
    }
}
