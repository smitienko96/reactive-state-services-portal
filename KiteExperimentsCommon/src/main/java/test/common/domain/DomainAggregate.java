package test.common.domain;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.core.annotation.AnnotationUtils;
import test.common.service.AggregateDeletedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public abstract class DomainAggregate<I extends AggregateIdentifier> {

    private static final String HANDLER_METHOD_NAMES = "on";

    private EventPublisher publisher;

    private I identifier;
    private AggregateVersion version;
    private Date lastModified;

    private boolean deleted;

    private List<DomainEvent> events = new ArrayList<>();

    protected DomainAggregate(EventPublisher publisher, I identifier) {
        this.publisher = publisher;
        this.identifier = identifier;
    }

    /**
     * Вызывается внутри конструктора нового агрегата или посредством иного механизма,
     * когда процесс создания агрегата завершен.
     */
    protected final void done() {
        raiseEvent(createInitialEvent());
    }

    protected DomainAggregate(EventPublisher publisher, I identifier, AggregateVersion version) {
        this.publisher = publisher;
        this.identifier = identifier;
        this.version = version;
    }

    protected void raiseEvent(DomainEvent event) {
        if (deleted) {
            log.error("Unable to raise event on deleted aggregate");
            throw new AggregateDeletedException(aggregateName(), identifier());
        }

        updateVersion();
        event.linkAggregate(this);
        events.add(event);

        boolean isCreation = event instanceof AggregateCreatedEvent;

        if (!isCreation && !isEventSourced()) {
            apply(event);
        }

        this.lastModified = new Date();

        // when event sourced publishe immediately
        // otherwise postpone till aggregate gets persisted
        if (isEventSourced()) {
            publisher.publish(event);
        }
    }

    private void updateVersion() {
        if (version == null) {
            version = AggregateVersion.initial();
        } else {
            version = version.increment();
        }
    }

    public AggregateVersion versionBeforeUpdate() {
        return new AggregateVersion(version.version() - events.size());
    }

    /**
     * Сливает текущие события агрегата поставщикам после его сохранения в
     * хранилище (только для классических, не ES агрегатов).
     */
    void flushEvents() {
        if (!isEventSourced() && !events.isEmpty()) {
            // flush all events when aggregate gets persisted
            publisher.publish(aggregateName(),
                    identifier(), events);
        }
    }

    /**
     * Помечает агрегат предметной области как удаленный.
     * Дальнейшие попытки работы с таким агрегатом приведут к исключению
     * {@link AggregateDeletedException}.
     */
    void markDeleted() {
        deleted = true;
    }


    void apply(DomainEvent event) {
        try {
            MethodUtils.invokeMethod(this, HANDLER_METHOD_NAMES, event);
        } catch (Exception ex) {
            log.error("Error occurred while replaying event [{}] for aggregate [{}] with identifier [{}]", event,
                    aggregateName(), identifier());

            publisher.publishError(ex);
        }
    }

    private boolean isEventSourced() {
        return AggregateMode.EVENT_SOURCING ==
                aggregateMetadata().mode();
    }

    /**
     * Получает метаданные агрегата
     *
     * @return
     */
    protected AggregateMetadata aggregateMetadata() {
        return AnnotationUtils.findAnnotation(getClass(),
                AggregateMetadata.class);
    }

    /**
     * Возвращает имя агрегата предметной области
     *
     * @return имя агрегата
     */
    String aggregateName() {
        return aggregateMetadata().name();
    }

    public abstract AggregateCreatedEvent<I, ?> createInitialEvent();

    public I identifier() {
        return identifier;
    }

    public AggregateVersion version() {
        return version;
    }

    public Date lastModified() {
        return new Date(lastModified.getTime());
    }
}
