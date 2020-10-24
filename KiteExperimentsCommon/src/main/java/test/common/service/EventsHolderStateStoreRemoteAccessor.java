package test.common.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.state.HostInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;

/**
 * Реализация удаленного доступа к хранителю событий агрегата через
 * хранилище состояний Kafka Streams на другом экземпляре процессора.
 * Для этого в сервисе, реализующем данный вид доступа, должен быть доступен
 * REST-endpoint {@link EventStoreConstants#KAFKA_REMOTE_STATE_STORE_PATH}
 */
@Slf4j
public class EventsHolderStateStoreRemoteAccessor implements IEventsHolderRemoteAccessor {

    @Autowired
    private InteractiveQueryService queryService;

    @Autowired
    private KafkaStoreConfigurationProperties configProperties;

    @Autowired
    private WebClient.Builder builder;

    @Override
    public Mono<AggregateEventsHolder> getAggregateEventsHolder(String aggregateName, String identifier) {
        String stateStoreName = configProperties.getLocalStore().getName();
        HostInfo aggregateHostInfo = queryService.getHostInfo(stateStoreName, identifier, Serdes.String().serializer());

                String url = MessageFormat.format("http://{0}:{1}{2}", aggregateHostInfo.host(),
                aggregateHostInfo.port(), EventStoreConstants.KAFKA_REMOTE_STATE_STORE_PATH);

        log.debug("Querying aggregate [{}] event stream from location [{}]", identifier, url);

        return builder.baseUrl(url).build().get()
                .uri("/{storeName}/{aggregateId}", stateStoreName, identifier)
                .accept(new MediaType(EventStoreConstants.KAFKA_EVENTS_STREAM_MIME_TYPE))
                .retrieve().bodyToMono(AggregateEventsHolder.class);
    }
}
