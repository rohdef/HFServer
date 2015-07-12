package dk.rohdef.hfserver;

import com.mongodb.Block;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by Rohde Fischer on 7/12/15.
 */
@Path("/hello")
public class Test  {
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    public Test() {
        mongoClient = new MongoClient();
        mongoDatabase = mongoClient.getDatabase("handicapformidlingen");
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
    public Tutorial create(Tutorial tutorial) {
        return tutorial;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
    public LinkedList<Tutorial> getTutorials() {
        MongoCollection<Document> tutorials = mongoDatabase.getCollection("tutorials");

        final LinkedList<Tutorial> tutorialLinkedListReturn = new LinkedList<>();
        FindIterable<Document> iterable = tutorials.find();
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                tutorialLinkedListReturn.add(documentToTutorial(document));
            }
        });

        return tutorialLinkedListReturn;
    }

//    @GET
//    @Path("/create")
//    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
//    public LinkedList<Tutorial> create() {
//        LinkedList<Document> tutorialLinkedList = new LinkedList<>();
//
//        Tutorial tutorial = new Tutorial();
//        tutorial.setContent("Is the things alive in ways you don't like? Then you should probably clean it.");
//        tutorial.setTitle("Cleaning fridge");
//        tutorial.setVideoId("39");
//        tutorial.setTags(new String[]{"fridge"});
//        tutorialLinkedList.add(tutorialToDocument(tutorial));
//
//        MongoCollection<Document> tutorials = mongoDatabase.getCollection("tutorials");
//        tutorials.insertMany(tutorialLinkedList);
//
//        final LinkedList<Tutorial> tutorialLinkedListReturn = new LinkedList<>();
//        FindIterable<Document> iterable = tutorials.find();
//        iterable.forEach(new Block<Document>() {
//            @Override
//            public void apply(final Document document) {
//                tutorialLinkedListReturn.add(documentToTutorial(document));
//            }
//        });
//
//        return tutorialLinkedListReturn;
//    }

    private Document tutorialToDocument(Tutorial tutorial) {
        return new Document("tutorial",
                new Document()
                        .append("title", tutorial.getTitle())
                        .append("content", tutorial.getContent())
                        .append("videoId", tutorial.getVideoId())
                        .append("tags", Arrays.asList(tutorial.getTags())));
    }

    private Tutorial documentToTutorial(Document document) {
        Document tutorialDocument = (Document) document.get("tutorial");

        Tutorial tutorial = new Tutorial();
        tutorial.setTitle(tutorialDocument.getString("title"));
        tutorial.setContent(tutorialDocument.getString("content"));
        tutorial.setVideoId(tutorialDocument.getString("videoId"));
        tutorial.setTags(new String[]{});

        return tutorial;
    }
}