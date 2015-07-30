package dk.rohdef.hfserver.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dk.rohdef.hfserver.model.LoginSession;
import org.bson.Document;

import java.util.GregorianCalendar;

/**
 * Created by rohdef on 7/13/15.
 */
public abstract class BaseService {
    private static ObjectMapper mapper = new ObjectMapper();
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

    public ObjectMapper getMapper() {
        return mapper;
    }

    public static Document toDocument(Object request) {
        try {
            Document userDocument = Document.parse(mapper.writeValueAsString(request));
            return userDocument;
        } catch (JsonProcessingException e) {
            e.printStackTrace();

            return null;
        }
    }
}
