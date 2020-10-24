package test.common.service;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import test.common.configuration.MongoConfiguration;

public class MongoClientFactory {

    private MongoClientFactory() {}

    /**
     *
     * @param mongoConfiguration
     * @return
     */
    public static MongoClient createClient(MongoConfiguration mongoConfiguration) {
        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(MongoCredential.createCredential(mongoConfiguration.getUsername(),
                        mongoConfiguration.getAuthenticationDb(),
                        mongoConfiguration.getPassword().toCharArray()))
                .applyConnectionString(new ConnectionString(mongoConfiguration.getDbAddress()))
                .build();

        return MongoClients.create(settings);
    }
}
