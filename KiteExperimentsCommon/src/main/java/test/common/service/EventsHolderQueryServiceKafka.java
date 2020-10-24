package test.common.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.state.HostInfo;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Сервис запроса хранителя событий агрегата из хранилища состояний
 * Kafka Streams.
 */
@Component
@Slf4j
@EnableConfigurationProperties(KafkaStoreConfigurationProperties.class)
public class EventsHolderQueryServiceKafka {

    private InteractiveQueryService queryService;

    @Autowired
    private IEventsHolderRemoteAccessor remoteAccessor;

    @Autowired
    private KafkaStoreConfigurationProperties configProperties;

    /**
     * Запрашивает хранитель событий агрегата, обрабатываемый KafkaStreams.
     *
     * @param aggregateId идентификатор агрегата
     * @param aggregateName
     * @return хранитель событий агрегата
     */
    public Mono<AggregateEventsHolder> getEventsHolderForAggregate(String aggregateId, String aggregateName) {
        return isFoundInStateStore(aggregateId)
                ? retrieveFromLocalStateStore(aggregateId)
                : retrieveFromRemote(aggregateName, aggregateId);
    }

    private boolean isFoundInStateStore(String aggregateId) {
        String stateStoreName = configProperties.getLocalStore().getName();
        HostInfo aggregateHostInfo = queryService.getHostInfo(stateStoreName, aggregateId, Serdes.String().serializer());

        return queryService.getCurrentHostInfo().equals(aggregateHostInfo);
    }

    private Mono<AggregateEventsHolder> retrieveFromLocalStateStore(String aggregateId) {
        String storeName = configProperties.getLocalStore().getName();

        ReadOnlyKeyValueStore<String, AggregateEventsHolder> localStore =
                queryService.getQueryableStore(storeName,
                        QueryableStoreTypes.keyValueStore());

        if (localStore == null) {
            return Mono.empty();
        }

        AggregateEventsHolder holder = localStore.get(aggregateId);
        return Mono.justOrEmpty(holder);
    }

    private Mono<AggregateEventsHolder> retrieveFromRemote(String aggregateName, String aggregateId) {
        return remoteAccessor.getAggregateEventsHolder(aggregateName, aggregateId);
    }

    @Autowired
    public void setQueryService(InteractiveQueryService queryService) {
        HostInfo hostInfo = queryService.getCurrentHostInfo();
        if (hostInfo == null) {
            throw new IllegalStateException("Configuration invalid! HostInfo for Kafka Interactive Query Service is not set!");
        }
        this.queryService = queryService;
    }
}
