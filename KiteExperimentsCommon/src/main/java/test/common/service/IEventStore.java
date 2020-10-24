package test.common.service;

import reactor.core.publisher.Mono;
import test.common.domain.*;

import java.util.List;

public interface IEventStore {

    /**
     * Загружает полный поток собьытий агрегата.
     *
     * @param aggregateName название агрегата
     * @param aggregateId   идентификатор агрегата
     * @return полный поток событий агрегата
     */
    Mono<EventStream> loadFullEventStream(String aggregateName,
                                          AggregateIdentifier aggregateId);

    /**
     * Загружает поток событий агрегата, произошедших с момента указанной
     * версии.
     *
     * @param aggregateName название агрегата
     * @param aggregateId   идентификатор агрегата
     * @return поток событий
     */
    Mono<EventStream> loadEventStreamSinceVersion(String aggregateName,
                                                  AggregateIdentifier aggregateId,
                                                  AggregateVersion version);

    /**
     * Загружает событие создания агрегата (начальное событие).
     *
     * @param aggregateName название агрегата
     * @param identifier    идентификатор агрегата
     * @return событие создания агрегата
     */
    Mono<DomainEvent> getInitialEvent(String aggregateName,
                                      AggregateIdentifier identifier);

    /**
     * @param aggregateName
     * @param identifier
     * @return
     */
    Mono<LastVersionHolder> getLastVersion(String aggregateName,
                                           AggregateIdentifier identifier);

    /**
     * Добавляет новое событие к потоку событий агрегата.
     *  @param aggregateName
     * @param aggregateId
     * @param event
     */
    Mono<Void> appendToStream(String aggregateName,
                              AggregateIdentifier aggregateId,
                              DomainEvent event);

    /**
     * Добавляет новые события к потоку событий агрегата.
     *
     * @param aggregateName
     * @param aggregateId
     * @param event
     */
    Mono<Void> appendToStream(String aggregateName,
                        AggregateIdentifier aggregateId,
                        List<DomainEvent> event);

}
