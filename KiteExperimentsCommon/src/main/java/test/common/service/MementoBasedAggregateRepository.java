package test.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import test.common.domain.*;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * @author s.smitienko
 */
public abstract class MementoBasedAggregateRepository<A extends DomainAggregate<I>,
        I extends AggregateIdentifier> implements IAggregateRepository<A,
        I> {

    @Autowired
    private IAggregateMementoService mementoService;

    @Autowired
    private IAggregateMementoBuilder<A> mementoBuilder;

    @Override
    public Mono<A> get(I id) {
        Map<String, Object> filterMap = buildUniquenessFilterMap(id, mementoBuilder);
        return checkAndReturnUnique(id.toString(), getByFilter(filterMap), true);
    }

    protected Map<String, Object> buildUniquenessFilterMap(I identifier, IAggregateMementoBuilder<A> builder) {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put(mementoBuilder.getIdentifierKey(),
                identifier.id());
        return filterMap;
    }

    @Override
    public Mono<Boolean> doSave(A aggregate) {
        Memento memento = mementoBuilder.backup(aggregate);
        Map<FilterAttribute, Object> filter = buildFilterForSave(aggregate.identifier(),
                aggregate.versionBeforeUpdate());
        return mementoService.storeMemento(aggregateName(),
                filter, memento).checkpoint();
    }

    @Override
    public Mono<Void> deleteAll() {
        return mementoService.deleteMemento(aggregateName(),
                new HashMap<>());
    }

    public Mono<Boolean> doDelete(A aggregate) {
        Map<String, Object> filterMap = buildUniquenessFilterMap(aggregate.identifier(), mementoBuilder);
        return mementoService.deleteMemento(aggregateName(), toFilterAttributes(filterMap))
                .thenReturn(true)
                .onErrorReturn(false);
    }

    private Map<FilterAttribute, Object> buildFilterForSave(AggregateIdentifier identifier,
                                        AggregateVersion version) {
        Map<FilterAttribute, Object> map = new HashMap<>();
        map.put(new FilterAttribute(mementoBuilder.getIdentifierKey(), true), identifier.id());
        map.put(new FilterAttribute(mementoBuilder.getVersionKey(), false), version.version());
        return map;
    }



    private Map<FilterAttribute, Object> toFilterAttributes(Map<String, Object> filterMap) {
        return filterMap.entrySet().stream()
                .collect(Collectors.toMap(e -> new FilterAttribute(e.getKey()), v -> v.getValue()));
    }

    protected Flux<A> getByFilter(Map<String, Object> filterMap) {
        Map<FilterAttribute, Object> filterAttributes = toFilterAttributes(filterMap);
        Flux<Memento> memento =
                mementoService.getMemento(aggregateName(),
                        filterAttributes).checkpoint(MessageFormat.format("{0} memento " +
                        "retrieved from " +
                        "repository", aggregateName()));

        return memento.map(mementoBuilder::restore);
    }

    protected Mono<A> checkAndReturnUnique(String identifier,
                                           Flux<A> series, boolean failOnEmpty) {
        Mono<A> single = series.single();

        if (failOnEmpty) {
            single = single
                    .onErrorMap(NoSuchElementException.class,
                            e -> new ElementNotFoundException(identifier));

        } else {
            single = single.onErrorResume(NoSuchElementException.class, e -> Mono.empty());
        }

        single = single.onErrorMap(IndexOutOfBoundsException.class,
                e -> new ElementNotUniqueException(identifier));
        return single;
    }

    protected abstract String aggregateName();
}
