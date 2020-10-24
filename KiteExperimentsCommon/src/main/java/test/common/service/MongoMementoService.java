package test.common.service;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import com.mongodb.reactivestreams.client.Success;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import test.common.configuration.MongoConfiguration;
import test.common.domain.Memento;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author s.smitienko
 */
@Component
//@EnableConfigurationProperties(MongoConfiguration.class)
@Slf4j
public class MongoMementoService implements IAggregateMementoService {

    private static final String ID_FIELD_NAME = "_id";

    private MongoClient mongoClient;

    private MongoConfiguration configuration;

    @Override
    public Flux<Memento> getMemento(String aggregateName,
                                    Map<FilterAttribute, Object> filterAttributes) {
        Bson filterBson = buildFilter(filterAttributes);

        return getCollection(aggregateName)
                .flatMapMany(documents -> Flux.from(documents.find(filterBson)))
                .map(Memento::new);
    }

    /**
     * @param aggregateName
     * @param values
     */
    @Override
    public Mono<Boolean> storeMemento(String aggregateName,
                                      Map<FilterAttribute, Object> filterAttributes, Memento values) {

        Document document =
                new Document(values.getState());
//        document.put(ID_FIELD_NAME, ObjectId.get());
        Mono<UpdateResult> result = getOrCreateCollection(aggregateName,
                filterAttributes)
                .flatMap(documents -> {
                    Bson filter = buildFilter(filterAttributes);

                    ReplaceOptions options = new ReplaceOptions();
                    options.upsert(true);

                    return Mono.from(documents.replaceOne(filter, document, options));
                });

        return result.map(update -> {
            boolean upserted = update.getUpsertedId() != null;
            boolean modified = update.getModifiedCount() > 0;

            if (upserted) {
                log.info("Inserted document with ID {}", update.getUpsertedId());
            } else if (modified) {
                log.info("Modified {} documents", update.getModifiedCount());
            }
            return upserted || modified;

        }).checkpoint();
    }

    @Override
    public Mono<Void> deleteMemento(String aggregateName, Map<FilterAttribute, Object> filterAttributes) {
        return getCollection(aggregateName)
                .filterWhen(collection -> Mono.from(collection.countDocuments()).map(count -> count > 0))
                .flatMap(collection -> {
                    Bson filter = buildFilter(filterAttributes);
                    return Mono.from(collection.deleteMany(filter));
                }).then().checkpoint();
    }

    private Mono<MongoCollection<Document>> getCollection(String aggregateName) {
        return getOrCreateCollection(aggregateName, null);
    }

    private Mono<MongoCollection<Document>> getOrCreateCollection(String aggregateName, Map<FilterAttribute, Object> attributes) {
        MongoDatabase database =
                mongoClient.getDatabase(configuration.getDatabase().getName());
        MongoCollection<Document> collection =
                database.getCollection(aggregateName);

        if (collection != null) {
            return Mono.just(collection);
        }

        if (MapUtils.isEmpty(attributes)) {
            return Mono.empty();
        }

        return Mono.from(database.createCollection(aggregateName))
                .<MongoCollection<Document>>handle((success, sink) -> {
                    if (Success.SUCCESS != success) {
                        sink.error(new RuntimeException(
                                String.format("Error occurred while creating new collection for aggregate [%s]", aggregateName)));
                        return;
                    }
                    sink.next(database.getCollection(aggregateName));
                    sink.complete();
                }).doOnSuccess(newCollection -> {
                    Flux.concat(attributes.keySet().stream().filter(FilterAttribute::isUnique)
                            .map(a -> newCollection.createIndex(Indexes.text(a.getAttributeName()),
                                    new IndexOptions().unique(true))).collect(Collectors.toList()))
                            .subscribe(index -> log.info("Index created with result [{}]", index));

                });
    }

    private Bson buildFilter(Map<FilterAttribute, Object> attributes) {
        List<Bson> subfilters = new ArrayList<>();
        attributes.forEach((attribute, value) -> {
            String attributeName = attribute.getAttributeName();
            Object filterValue = attribute.equals(ID_FIELD_NAME) ?
                    new ObjectId((String) value) : value;
            subfilters.add(Filters.eq(attributeName, filterValue));
        });
        Bson result = null;
        for (Bson filter : subfilters) {
            if (result == null) {
                result = filter;
            } else {
                result = Filters.and(result, filter);
            }
        }
        return result;
    }

    @Autowired
    @Qualifier("mementoMongoConfiguration")
    public void setConfiguration(MongoConfiguration configuration) {
        this.configuration = configuration;
        this.mongoClient = MongoClientFactory.createClient(configuration);
    }
}
