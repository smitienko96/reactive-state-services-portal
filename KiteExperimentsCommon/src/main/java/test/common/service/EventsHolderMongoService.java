package test.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.kafka.streams.SendToDlqAndContinue;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import test.common.configuration.MongoConfiguration;

/**
 * Реализация удаленного доступа к хранителю событий агрегата через
 * коллекцию документов MongoDB.
 */
@Component
@EnableBinding(KafkaEventProcessor.class)
@Slf4j
@EnableAutoConfiguration
public class EventsHolderMongoService implements IEventsHolderRemoteAccessor {

    private static final String AGGREGATE_IDENTIFIER_FIELD = "aggregateIdentifier";

    private MongoClient mongoClient;

    private String databaseName;

    @Autowired
    private SendToDlqAndContinue dlqHandler;

    private ObjectMapper objectMapper;

    public EventsHolderMongoService() {
        this.objectMapper = ObjectMapperFactory.
                createKafkaStateStoreObjectMapper();
    }

    @StreamListener(KafkaEventProcessor.PROJECTING_INCOMING)
    public void projectIncoming(KStream<String, AggregateEventsHolder> eventsHolderStream) {
        eventsHolderStream.process(() -> new Processor<String, AggregateEventsHolder>() {

            private ProcessorContext context;

            @Override
            public void init(ProcessorContext context) {
                this.context = context;
            }

            private Serializer<String> getKeySerializer() {
                return (Serializer<String>) context.keySerde().serializer();
            }

            private Serializer<AggregateEventsHolder> getValueSerializer() {
                return (Serializer<AggregateEventsHolder>) context.valueSerde().serializer();
            }

            @Override
            public void process(String aggregateIdentifier, AggregateEventsHolder holder) {
                log.info("Storing events holder for aggregate [{}] to Mongo. Holder: [{}]", aggregateIdentifier, holder);
                saveAggregateEventsHolder(aggregateIdentifier, holder)
                        .doOnError(throwable -> {
                            byte[] keyBytes = getKeySerializer().serialize(KafkaEventProcessor.DLQ_OUTGOING, aggregateIdentifier);
                            byte[] valueBytes = getValueSerializer().serialize(KafkaEventProcessor.DLQ_OUTGOING, holder);
                            dlqHandler.sendToDlq(KafkaEventProcessor.DLQ_OUTGOING, keyBytes, valueBytes, context.partition());
                        })
                        .block();
            }

            @Override
            public void close() {

            }
        });
    }

    /**
     * @param identifier
     * @param holder
     */
    private Mono<Void> saveAggregateEventsHolder(String identifier, AggregateEventsHolder holder) {
        String aggregateName = holder.aggregateName();
        Mono<MongoCollection<Document>> holderCollection = getAggregateHolderCollection(aggregateName);

        return holderCollection.flatMap(collection -> {
            Document holderDocument = convertHolderToDocument(identifier, holder);
            Bson replacementFilter = buildFilter(identifier);

            Publisher<UpdateResult> result = collection.replaceOne(replacementFilter, holderDocument,
                    new ReplaceOptions().upsert(true));
            return Mono.from(result).doOnSuccess(update -> {
                if (update.getModifiedCount() > 0) {
                    log.info("{} aggregate holder updated for filter [{}] and aggregate [{}]",
                            update.getModifiedCount(), replacementFilter, aggregateName);
                } else if (update.getUpsertedId() != null) {
                    log.info("New holder [{}] upserted for aggregate [{}]", update.getUpsertedId(), aggregateName);
                }
            });
        }).then();
    }

    /**
     * @param aggregateName
     * @param identifier
     * @return
     */
    public Mono<AggregateEventsHolder> getAggregateEventsHolder(String aggregateName, String identifier) {
        Mono<MongoCollection<Document>> holderCollection = getAggregateHolderCollection(aggregateName);
        return holderCollection.flatMap(collection -> {
            FindPublisher<Document> documents = collection.find(buildFilter(identifier));
            return Mono.from(documents);
        }).map(this::convertDocumentToHolder);
    }

    private Bson buildFilter(String identifier) {
        return Filters.eq(AGGREGATE_IDENTIFIER_FIELD, identifier);
    }

    private Document convertHolderToDocument(String identifier, AggregateEventsHolder holder) {
        try {
            EventsHolderDocument holderDocument = new EventsHolderDocument(identifier, holder);
            String jsonString = objectMapper.writeValueAsString(holderDocument);
            return Document.parse(jsonString);
        } catch (JsonProcessingException ex) {
            log.error("Error occurred while serializing aggregate events holder to string", ex);
            throw new RuntimeException(ex);
        }
    }

    private AggregateEventsHolder convertDocumentToHolder(Document document) {
        String jsonString = document.toJson(buildWriterSettings());
        try {
            EventsHolderDocument holderDocument = objectMapper.readValue(jsonString, EventsHolderDocument.class);
            return holderDocument.getHolder();
        } catch (Exception ex) {
            log.error("Error occurred while deserializing aggregate events holder from document [{}]",
                    document.getObjectId("_id"), ex);
            throw new RuntimeException(ex);
        }
    }

    private static JsonWriterSettings buildWriterSettings() {
        return JsonWriterSettings.builder()
                .int64Converter((value, writer) -> writer.writeNumber(value.toString()))
                .build();
    }

    private Mono<MongoCollection<Document>> getAggregateHolderCollection(String aggregateName) {
        MongoDatabase database = mongoClient.getDatabase(databaseName);

        MongoCollection collection = database.getCollection(aggregateName);

        if (collection != null) {
            return Mono.just(collection);
        }

        return Mono.from(database.createCollection(aggregateName))
                .map(success -> {
                    if (Success.SUCCESS != success) {
                        throw new RuntimeException(
                                String.format("Error occurred while creating new collection for aggregate [%s]", aggregateName));
                    }
                    return database.getCollection(aggregateName);
                }).doOnNext(newCollection -> {
                    Mono.from(newCollection.createIndex(Indexes.text(AGGREGATE_IDENTIFIER_FIELD),
                            new IndexOptions().unique(true)))
                            .block();
                    log.info("Index created with result [{}]", AGGREGATE_IDENTIFIER_FIELD);
                });
    }

    @Autowired
    @Qualifier("eventStoreMongoConfiguration")
    public void setConfiguration(MongoConfiguration configuration) {
        this.mongoClient = MongoClientFactory.createClient(configuration);
    }

    @Autowired
    public void setStoreProperties(KafkaStoreConfigurationProperties storeProperties) {
        this.databaseName = storeProperties.getLocalStore().getName();
    }

}
