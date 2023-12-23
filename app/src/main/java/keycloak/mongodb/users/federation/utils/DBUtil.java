package keycloak.mongodb.users.federation.utils;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

import keycloak.mongodb.users.federation.config.MongoClientConnector;
import keycloak.mongodb.users.federation.dto.User;


public class DBUtil {

    public static List<User> getUserByUsername(String username) {
        MongoCollection<User> usersCollection = MongoClientConnector.getMongoDatabase().getCollection("users",
                User.class);
        Document searchQuery = new Document();
        searchQuery.put("username", username);
        List<User> users = new ArrayList<>();
        usersCollection.find(searchQuery).forEach(user -> {
            users.add(user);
        });

        return users;
    }

    public static List<User> getAllUsers(){
        MongoCollection<User> usersCollection = MongoClientConnector.getMongoDatabase().getCollection("users",
                User.class);
        
        List<User> users = new ArrayList<>();
        usersCollection.find().forEach(user -> {
            users.add(user);
        });

        return users;
    }
}
