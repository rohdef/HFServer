package dk.rohdef.hfserver.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import dk.rohdef.hfserver.model.LoginRequest;
import dk.rohdef.hfserver.model.LoginSession;
import dk.rohdef.hfserver.model.User;
import dk.rohdef.hfserver.model.UserType;
import org.bson.Document;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.*;

/**
 * Created by rohdef on 7/13/15.
 */
@Api(value = "/auth", description = "A simple mock login service, might be replaced with a proper OAuth service. Current timeout is 2 mins.")
@Path("/auth")
@PermitAll
public class LoginService extends BaseService {
    public static final int MINS = 2;

    @POST
    @Path("/login")
    @ApiOperation(value = "Login method",
            position = 1)
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
    @PermitAll
    public LoginSession login(LoginRequest loginRequest) {
        MongoCollection<Document> users = getMongoDatabase().getCollection("users");
        FindIterable<Document> usersFound = users.find(toDocument(loginRequest));
        final LinkedList<User> userLinkedList = new LinkedList<>();

        usersFound.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                userLinkedList.add(documentToUser(document));
            }
        });

        LoginSession loginSession = null;
        boolean successful = (userLinkedList.size() == 1);

        if (successful) {
            User user = userLinkedList.getFirst();
            GregorianCalendar cal = new GregorianCalendar();
            cal.add(Calendar.MINUTE, MINS);

            loginSession = new LoginSession(true,
                    "OK",
                    user.getUserName(),
                    user.getUserType(),
                    UUID.randomUUID(),
                    cal.getTime()
                    );

            MongoCollection<Document> loginSessions = getMongoDatabase().getCollection("loginSessions");
            loginSessions.deleteMany(new Document().append("userName", user.getUserName()));
            loginSessions.insertOne(toDocument(loginSession));
        } else if (userLinkedList.size() == 0) {
            loginSession = new LoginSession(false,
                    "Wrong username or password",
                    loginRequest.getUserName());
        }

        return loginSession;
    }

    @POST
    @Path("/logout")
    @ApiOperation(value = "Logout user",
            position = 3)
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
    @RolesAllowed("helper,citizen,admin")
    public void logout (LoginSession loginSession) {
        MongoCollection<Document> loginSessions = getMongoDatabase().getCollection("loginSessions");
        loginSessions.deleteMany(new Document().append("userName", loginSession.getUserName()));
    }

    @GET
    @Path("/create")
    @ApiOperation(value = "Create demonstration users",
            position = 4)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
    @RolesAllowed("admin")
    public LinkedList<User> create() {
        LinkedList<Document> userLinkedList = new LinkedList<>();

        userLinkedList.add(toDocument(new User("rohdef",
                "s3cr3t",
                UserType.CITIZEN,
                "Rohde",
                "Fischer",
                "Kirkegårdsvej",
                8000,
                "rohdef@rohdef.dk",
                21680621)));
        userLinkedList.add(toDocument(new User("kim",
                "m4g1c",
                UserType.HELPER,
                "Kim",
                "Andersen",
                "Mejlgade",
                8000,
                "kim@magi.dk",
                66554433)));
        userLinkedList.add(toDocument(new User("klaus",
                "0wn3r",
                UserType.ADMIN,
                "Klaus",
                "Dube",
                "Some Street",
                1200,
                "klaus@handicapformidlingen.dk",
                22334455)));

        MongoCollection<Document> users = getMongoDatabase().getCollection("users");
        users.deleteMany(new Document());
        users.insertMany(userLinkedList);

        final LinkedList<User> userLinkedListReturn = new LinkedList<>();
        FindIterable<Document> iterable = users.find();
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                userLinkedListReturn.add(documentToUser(document));
            }
        });

        return userLinkedListReturn;
    }

    private User documentToUser(Document document) {
        try {
            User user = getMapper().readValue(document.toJson(), User.class);
            return user;
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

}
