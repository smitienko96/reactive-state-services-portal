package test.common.domain;

public abstract class AggregateBuilder<A extends DomainAggregate> {

    public enum Mode {
        FRESH, RESTORE;
    }

    protected EventPublisher publisher;
    protected AggregateVersion version;
    private Mode mode;

    protected AggregateBuilder(EventPublisher publisher, AggregateVersion version) {
        this.publisher = publisher;
        this.version = version;
        this.mode = AggregateVersion.absent().equals(version)
                ? Mode.FRESH : Mode.RESTORE;
    }

    protected AggregateBuilder(EventPublisher publisher) {
        this(publisher, AggregateVersion.absent());
    }

    public final BuilderResult<A> build() {
        final A aggregate;
        if (mode == Mode.FRESH) {
            aggregate = assemblyFresh();
            aggregate.done();
        } else if (mode == Mode.RESTORE) {
            aggregate = assemblyRestore();
        } else {
            throw new RuntimeException("Mode not implemented");
        }

        boolean saveRequired = aggregate.aggregateMetadata().mode() == AggregateMode.CLASSIC;

        return new BuilderResult(aggregate, saveRequired);
    }

    protected abstract A assemblyFresh();

    protected abstract A assemblyRestore();

}
