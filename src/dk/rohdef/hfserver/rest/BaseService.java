package dk.rohdef.hfserver.rest;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * Created by rohdef on 7/13/15.
 */
public abstract class BaseService {
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    public BaseService() {
        mongoClient = new MongoClient();
        mongoDatabase = mongoClient.getDatabase("handicapformidlingen");
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }
}
