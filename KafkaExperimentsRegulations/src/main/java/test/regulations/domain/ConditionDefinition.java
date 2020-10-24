package test.regulations.domain;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ConditionDefinition {

    private String name;
    private String description;

    public ConditionDefinition(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String name() { return name; }

    public String description() { return description; }
}
