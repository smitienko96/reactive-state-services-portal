package test.common.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode
public class AggregateVersion {

    private int version;

    public AggregateVersion(int version) {
        this.version = version;
    }

    public int version() {
        return version;
    }

    public static AggregateVersion absent() {
        return new AggregateVersion(0);
    }

    public static AggregateVersion initial() {
        return new AggregateVersion(1);
    }

    public AggregateVersion increment() {
        return new AggregateVersion(version + 1);
    }

    public boolean isGreater(AggregateVersion version) {
        return version() > version.version();
    }

    public int difference(AggregateVersion version) {
        return this.version - version.version();
    }
}
