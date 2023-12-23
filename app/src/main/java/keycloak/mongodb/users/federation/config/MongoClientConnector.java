package keycloak.mongodb.users.federation.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoClientConnector {
    public static MongoClient mongoClient = null;

    public static MongoClient getMongoClient(){
        if(mongoClient==null)
            mongoClient = MongoClients.create("mongodb://localhost:27017");
        return mongoClient;
    }

    public static MongoDatabase getMongoDatabase(){
        return getMongoClient().getDatabase("myMongo");
    }
}
