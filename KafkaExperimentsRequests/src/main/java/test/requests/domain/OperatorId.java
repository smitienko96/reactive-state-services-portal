package test.requests.domain;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OperatorId {
    private String id;

    public OperatorId(String id) {
        this.id = id;
    }

    public String id() { return id; }
}
