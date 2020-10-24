package test.common.domain;

import reactor.core.publisher.Mono;

/**
 * @author s.smitienko
 */
public interface IAggregateRepository<A extends DomainAggregate<I>,
        I extends AggregateIdentifier> {

    /**
     * Возвращает агрегат с указанным идентификатором или выбрасывает
     * исключение {@link test.common.service.ElementNotFoundException}.
     *
     * @param identifier идентификатор агрегата
     * @return агрегат
     */
    Mono<A> get(I identifier);

    /**
     * Сохраняет агрегат в в хранилище и "сливает" все порожденные за текущую
     * транзакцию события.
     *
     * @param aggregate агрегат
     * @return
     */
    default Mono<Boolean> save(A aggregate) {
        return doSave(aggregate).doOnSuccess(success -> {
            if (success) {
                aggregate.flushEvents();
            }
        });
    }


    /**
     * @param aggregate
     * @return
     */
    Mono<Boolean> doSave(A aggregate);

    /**
     * Удаляет агрегат предметной области из системы
     *
     * @param aggregate
     * @return
     */
    default Mono<Boolean> delete(A aggregate) {
        return doDelete(aggregate).doOnSuccess(success -> {
            if (success) {
                aggregate.markDeleted();
            }
        });
    }

    /**
     * Выполняет удаление агрегата предметной области из системы
     * @param aggregate
     * @return
     */
    Mono<Boolean> doDelete(A aggregate);

    /**
     * Удаляет агрегат по его идентификатору
     *
     * @param identifier
     * @return
     */
    default Mono<Boolean> delete(I identifier) {
        return get(identifier).flatMap(this::delete)
                .switchIfEmpty(
                        Mono.error(new RuntimeException(
                                String.format("Identifier with [%s] doesn't exist",
                                        identifier.id()))));
    }

    /**
     * Удаляет все хранящиеся в репозитории агрегаты.
     * По умолчанию данный метод не делает ничего, но реализации могут
     * включить такую возможность в случае необходимости.
     *
     * @return
     */
    default Mono<Void> deleteAll() {
        // do none
        return Mono.empty();
    }

}
