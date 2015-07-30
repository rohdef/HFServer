package dk.rohdef.hfserver.jersey.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dk.rohdef.hfserver.model.LoginSession;
import dk.rohdef.hfserver.model.User;
import dk.rohdef.hfserver.model.UserType;
import dk.rohdef.hfserver.rest.BaseService;
import dk.rohdef.hfserver.rest.LoginService;
import org.bson.Document;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.UUID;

/**
 * Jersey HTTP Basic Auth filter
 * @author Rohde Fischer
 */
@PreMatching
@Provider
public class AuthFilter implements ContainerRequestFilter {
    private static ObjectMapper mapper = new ObjectMapper();
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    public AuthFilter() {
        mongoClient = new MongoClient();
        mongoDatabase = mongoClient.getDatabase("handicapformidlingen");
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String auth = requestContext.getHeaders().getFirst("authorization");

        if(auth == null){
            return;
        }

        String[] loginAndToken = BasicAuth.decode(auth);

        //If login or password fail
        if(loginAndToken == null || loginAndToken.length != 2){
            return;
        }

        LoginSession loginSession = getLoginSession(loginAndToken[0], loginAndToken[1]);

        SecurityContext securityContext = new HfSecurityContext(loginSession);
        requestContext.setSecurityContext(securityContext);

    }

    private LoginSession getLoginSession(String userName, String token) {
        final LinkedList<LoginSession> loginSessions = new LinkedList<>();
        LoginSession loginSession = null;

        GregorianCalendar now = new GregorianCalendar();
        GregorianCalendar cal = new GregorianCalendar();

        final MongoCollection<Document> sessionCollection = mongoDatabase.getCollection("loginSessions");
        Document session = new Document()
                .append("userName", userName)
                .append("token", token);
        FindIterable<Document> sessionsFound = sessionCollection.find(session);
        sessionsFound.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                loginSessions.add(documentToLoginSession(document));
            }
        });

        if (loginSessions.size() == 1) {
            loginSession = loginSessions.getFirst();
            cal.setTime(loginSession.getTime());
        }

        if (loginSession != null && now.compareTo(cal) < 0) {
            now.add(Calendar.MINUTE, LoginService.MINS);

            loginSession.setTime(now.getTime());
            Document sessionDocument = BaseService.toDocument(loginSession);
            sessionDocument.remove("_id");
            sessionCollection.replaceOne(new Document().append("userName", userName),
                    sessionDocument);
            loginSession.setAccess(true);
        } else {
            loginSession = null;
            sessionCollection.deleteMany(new Document().append("userName", userName));
        }

        return loginSession;
    }

    private LoginSession documentToLoginSession(Document document) {
        try {
            String json = document.toJson();
            json = json.replaceAll("\\{\\s*\"\\$numberLong\\\"\\s*:\\s*(\\\"\\d+\\\")\\s*\\}",
                    "$1");
            LoginSession loginSession = mapper.readValue(json, LoginSession.class);
            return loginSession;
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }
}
