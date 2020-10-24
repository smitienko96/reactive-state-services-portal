package test.common.domain;

import java.util.Iterator;
import java.util.List;

public class EventStream implements Iterator<DomainEvent> {

    private String aggregateName;
    private AggregateIdentifier aggregateId;

    private Iterator<DomainEvent> domainEventsIter;

    private AggregateVersion version;

    private boolean deleted;

    /**
     * Конструктор потока событий агрегата
     *
     * @param aggregateName название агрегата
     * @param aggregateId   идентификатор агрегата
     * @param domainEvents  список событий агрегата
     * @param version       версия агрегата
     */
    public EventStream(String aggregateName,
                       AggregateIdentifier aggregateId,
                       List<DomainEvent> domainEvents,
                       AggregateVersion version,
                       boolean deleted) {

        this.aggregateName = aggregateName;
        this.aggregateId = aggregateId;
        this.domainEventsIter = domainEvents.iterator();
        this.version = version;
        this.deleted = deleted;
    }


    /**
     * Название агрегата
     *
     * @return название агрегата
     */
    public String getAggregateName() {
        return aggregateName;
    }

    /**
     * Идентификатор агрегата
     *
     * @return идентификатор
     */
    public AggregateIdentifier getAggregateId() {
        return aggregateId;
    }

    @Override
    public boolean hasNext() {
        return domainEventsIter.hasNext();
    }

    @Override
    public DomainEvent next() {
        return domainEventsIter.next();
    }

    /**
     * Возвращает версию агрегата, для которой возвращается данный поток
     * событий. Если возвращается начальная версия
     * ({@link AggregateVersion#initial()}), то поток событий содержит полную
     * историю работы с агрегатом с самого момента его создания.
     *
     * @return версия агрегата.
     */
    public AggregateVersion getVersion() {
        return version;
    }

    public boolean isDeleted() { return deleted; }
}