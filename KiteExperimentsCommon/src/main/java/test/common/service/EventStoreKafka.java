package test.common.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import test.common.domain.*;

import java.util.Collections;
import java.util.List;

/**
 * @author s.smitienko
 */
@Slf4j
@EnableBinding(KafkaEventProcessor.class)
@EnableConfigurationProperties(KafkaStoreConfigurationProperties.class)
@Component
@EnableAutoConfiguration
public class EventStoreKafka implements IEventStore {

    private static final String AGGREGATE_NAME_HEADER = "aggregateName";

    @Autowired
    private KafkaStoreConfigurationProperties configProperties;

    @Autowired
    private KafkaEventProcessor processorBinding;

    @Autowired
    private EventConversionService conversionService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private EventsHolderQueryServiceKafka eventsHolderQueryService;


    @Override
    public Mono<EventStream> loadFullEventStream(String aggregateName,
                                                 AggregateIdentifier aggregateId) {
        Mono<AggregateEventsHolder> eventsHolder =
                getEventsHolder(aggregateName, aggregateId);
        return buildEventStream(aggregateName, aggregateId,
                eventsHolder);
    }

    @Override
    public Mono<EventStream> loadEventStreamSinceVersion(String aggregateName, AggregateIdentifier aggregateId, AggregateVersion version) {
        throw new RuntimeException("Operation not supported!");
    }

    @Override
    public Mono<DomainEvent> getInitialEvent(String aggregateName, AggregateIdentifier identifier) {
        Mono<AggregateEventsHolder> eventsHolder =
                getEventsHolder(aggregateName, identifier);
        return eventsHolder.map(h -> h.getInitialEvent());

    }

    @Override
    public Mono<LastVersionHolder> getLastVersion(String aggregateName, AggregateIdentifier identifier) {
        Mono<AggregateEventsHolder> eventsHolder =
                getEventsHolder(aggregateName, identifier);
        return eventsHolder.map(this::buildLastVersionHolder);
    }

    private LastVersionHolder buildLastVersionHolder(AggregateEventsHolder eventsHolder) {
        AggregateVersion lastVersion = new AggregateVersion(eventsHolder.getVersion());
        return new LastVersionHolder(lastVersion, eventsHolder.isShadow());
    }

    @Override
    public Mono<Void> appendToStream(String aggregateName,
                                     AggregateIdentifier aggregateId, DomainEvent event) {
        Mono<LastVersionHolder> lastVersion = getLastVersion(aggregateName,
                aggregateId);

        Mono<Boolean> result = lastVersion
                .switchIfEmpty(Mono.just(LastVersionHolder.absent()))
                .handle((holder, sink) -> {
                    checkHandleDomainEvent(event, holder);
                    Message<SpecificRecord> eventMessage =
                            buildMessage(aggregateName,
                                    aggregateId,
                                    event);
                    if (eventMessage != null) {
                        sink.next(processorBinding.outgoing().send(eventMessage));
                    }
                    sink.complete();
                });

        return result.then();
    }

    /**
     *
     * @param event
     * @param holder
     * @throws AggregateConcurrentWriteException
     */
    private void checkHandleDomainEvent(DomainEvent event, LastVersionHolder holder) {
        AggregateVersion lastVersion = holder.lastVersion();
        if (conversionService.isInitialDomainEvent(event)) {
            if (holder.isShadow()) {
                log.info("Attempting to restore aggregate [{}] from shadow state.", event.aggregateIdentifier());
            } else if (lastVersion.isGreater(AggregateVersion.absent())) {
                throw new AggregateConcurrentWriteException(
                        "Unable to insert initial event for already existing aggregate");
            }
        } else if (holder.isShadow()) {
            throw new AggregateDeletedException(event.aggregateName(),
                    event.aggregateIdentifier());
        }

        if (lastVersion.isGreater(event.version())) {
            throw new AggregateConcurrentWriteException();
        }
    }

    private Message<SpecificRecord> buildMessage(String aggregateName,
                                                 AggregateIdentifier identifier,
                                                 DomainEvent event) {
        final SpecificRecord record;
        try {
            record = conversionService.toAvroRecord(event);
        } catch (EventConversionException ex) {
            throw new RuntimeException(ex);
        }

        return MessageBuilder.withPayload(record).setHeader(AGGREGATE_NAME_HEADER, aggregateName)
                .setHeader(KafkaHeaders.MESSAGE_KEY, identifier.id()).build();
    }

    @Override
    public Mono<Void> appendToStream(String aggregateName,
                                     AggregateIdentifier aggregateId,
                                     List<DomainEvent> events) {
        return
                events.stream().map(event -> appendToStream(aggregateName,
                        aggregateId,
                        event)).reduce(Mono.empty(), (a, b) -> a.then(b));
    }

    @StreamListener(KafkaEventProcessor.INCOMING)
    @SendTo(KafkaEventProcessor.PROJECTING_OUTGOING)
    public KStream<String, AggregateEventsHolder> process(KStream<String,
            SpecificRecord> recordStream) {
        KStream<String, SpecificRecord> eventStream = recordStream
                .filter((id, record) -> conversionService.isEventRecord(record));

        if (!supportsLocalStore()) {
            return eventStream.flatMap((identifier, eventRecord) -> {
                try {
                    if (checkDuplication(eventRecord)) {
                        processEvent(identifier, eventRecord);
                    }
                } catch (EventProcessorException ex) {
                    sendToQuarantine(identifier, eventRecord, ex);
                }
                return Collections.emptyList();
            });
        }

        return eventStream
                .groupByKey()
                .aggregate(AggregateEventsHolder::new,
                        (identifier,
                         eventRecord, aggregate) -> {
                            if (!checkDuplication(eventRecord, aggregate)) {
                                return aggregate;
                            }
                            AggregateEventsHolder holder = aggregate;
                            try {

                                if (conversionService.isDomainEventRecord(eventRecord)) {
                                    holder =  updateEventsHolder(eventRecord, aggregate);
                                    log.info("Event holder for aggregate [{}] successfully updated with event: [{}]. Holder: [{}]",
                                            identifier, eventRecord, holder);
                                }
                                processEvent(identifier, eventRecord);
                            } catch (EventProcessorException ex) {
                                sendToQuarantine(identifier, eventRecord, ex);
                            }
                            return holder;
                        },
                        getLocalStore())
                // Шаг 3: отправляем хранитель агрегата в поток PROJECTING_OUTGOING
                .toStream();


    }

    private boolean checkDuplication(SpecificRecord record) {
        return checkDuplication(record, null);
    }

    private boolean checkDuplication(SpecificRecord record, AggregateEventsHolder eventsHolder) {
        if (!conversionService.isDomainEventRecord(record)) {
            // currently skip all foreign events
            return true;
        }

        if (eventsHolder == null) {
            return true;
        }

        try {
            DomainEvent domainEvent = conversionService.fromAvroRecord(record);
            checkHandleDomainEvent(domainEvent, buildLastVersionHolder(eventsHolder));
            return true;
        } catch (Exception ex) {
            log.warn("Duplicate for event detected [{}]", record);
            return false;
        }
    }


    private void sendToQuarantine(String key, SpecificRecord eventRecord,
                                  EventProcessorException ex) {
        processorBinding.quarantineOutgoing().send(MessageBuilder.withPayload(eventRecord)
                .setHeader(KafkaHeaders.MESSAGE_KEY, key)
//                .setHeader(PROCESSING_PHASE_HEADER, phase.name())
                .setHeader(KafkaHeaders.DLT_EXCEPTION_MESSAGE, ex.getMessage())
                .setHeader(KafkaHeaders.DLT_EXCEPTION_STACKTRACE, ex.getStackTrace()).build());
    }

    /**
     * Определеяет поддерживается ли текущей конфигурацией сохранение событий в
     * локальное хранилище
     *
     * @return сохранение в локальное хранилище поддерживается
     */
    private boolean supportsLocalStore() {
        return configProperties.getLocalStore().isEnable();
    }

    private AggregateEventsHolder updateEventsHolder(SpecificRecord record,
                                                     AggregateEventsHolder holder) throws EventProcessorException {
        try {
            DomainEvent event = conversionService.fromAvroRecord(record);
            holder.addEvent(event);

            if (conversionService.isInitialDomainEvent(event)) {
                holder.setInitialEvent(event);
            }
            holder.setVersion(event.version().version());
            holder.setShadow(conversionService.isShadowingDomainEvent(event));

            return holder;
        } catch (Exception ex) {
            throw new EventProcessorException(ex);
        }

    }


    /**
     * Получает локальное хранилище данного процессора стримов
     * для хранения потока событий.
     *
     * @return
     */
    private Materialized<String, AggregateEventsHolder,
            KeyValueStore<Bytes, byte[]>>
    getLocalStore() {
        String storeName = configProperties.getLocalStore().getName();
        return Materialized.<String, AggregateEventsHolder,
                KeyValueStore<Bytes, byte[]>>as(storeName)
                .withKeySerde(Serdes.String())
                .withValueSerde(new EventHolderSerde());
    }

    private Mono<AggregateEventsHolder> getEventsHolder(String aggregateName, AggregateIdentifier aggregateId) {
        return eventsHolderQueryService.getEventsHolderForAggregate(aggregateId.id(),
                aggregateName);
    }

    /**
     * Обрабатывает событие предметной области, сохраняя его в репозитории
     * событий и прогоняя через цепочку локальных слушателей.
     *
     * @param aggregateId идентификатор агрегата предметной области
     * @param eventRecord запись события предметной области
     */
    protected void processEvent(String aggregateId, SpecificRecord eventRecord) throws EventProcessorException {
        try {
            Event event =
                    conversionService.fromAvroRecord(eventRecord);

            log.debug("Event fetched from store [{0}]", event);
            eventPublisher.publishEvent(event);
        } catch (EventConversionException ex) {
            log.error("Conversion of avro event record for aggregate [{}] failed. Skipping event processing", aggregateId, ex);
        } catch (Throwable ex) {
            throw new EventProcessorException(ex);
        }
    }


    private Mono<EventStream> buildEventStream(String aggregateName,
                                               AggregateIdentifier aggregateId,
                                               Mono<AggregateEventsHolder> eventsHolder) {

        return eventsHolder.map(h -> new EventStream(aggregateName, aggregateId,
                retrieveDomainEventsFromHolder(h),
                new AggregateVersion(h.getVersion()), h.isShadow()));
    }

    private List<DomainEvent> retrieveDomainEventsFromHolder(AggregateEventsHolder holder) {
        return holder.getEvents();
    }

}
