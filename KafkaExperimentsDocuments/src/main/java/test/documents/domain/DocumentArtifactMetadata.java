package test.documents.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class DocumentArtifactMetadata {

    private String name;
    private String description;

    public DocumentArtifactMetadata(String name) {
        this(name, "");
    }

    public DocumentArtifactMetadata(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public String name() {
        return name;
    }

    /**
     * Значение <поле>
     *
     * @return значение <поле>
     */
    public String description() {
        return description;
    }
}
