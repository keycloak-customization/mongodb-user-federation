package keycloak.mongodb.users.federation.config;

import static org.bson.codecs.configuration.CodecRegistries.*;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoClientConnector {
    public static MongoClient mongoClient = null;

    public static MongoClient getMongoClient() {
        if (mongoClient == null) {
            CodecRegistry pojoCodecRegistry = fromRegistries(
                    MongoClientSettings.getDefaultCodecRegistry(),
                    fromProviders(PojoCodecProvider.builder().automatic(true).build()));

            MongoClientSettings settings = MongoClientSettings.builder()
                    .codecRegistry(pojoCodecRegistry)
                    .applyConnectionString(new ConnectionString("mongodb://localhost:27017"))
                    .build();
            mongoClient = MongoClients.create(settings);
        }

        return mongoClient;
    }

    public static MongoDatabase getMongoDatabase() {
        return getMongoClient().getDatabase("myMongo");
    }
}
