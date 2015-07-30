package dk.rohdef.hfserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dk.rohdef.hfserver.jersey.ObjectIdDeserializer;
import dk.rohdef.hfserver.jersey.ObjectIdSerializer;
import org.bson.types.ObjectId;

import javax.xml.bind.annotation.XmlRootElement;
import java.security.Principal;
import java.util.Date;
import java.util.UUID;

/**
 * Created by rohdef on 7/13/15.
 */
@XmlRootElement
public class LoginSession implements Principal {
    @JsonProperty("_id")
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    @JsonSerialize(using = ObjectIdSerializer.class)
    private String id;

    @JsonIgnore
    private boolean access;

    private String userName, message;
    private UserType userType;
    private UUID token;
    private Date time;

    public LoginSession() {
        id = ObjectId.get().toHexString();
    }

    public LoginSession(boolean access, String userName, String message) {
        this(access, userName, message, UserType.NO_ACCESS, null, null);
    }

    public LoginSession(boolean access, String message, String userName, UserType userType, UUID token, Date time) {
        this();
        this.access = access;
        this.message = message;
        this.userName = userName;
        this.userType = userType;
        this.token = token;
        this.time = time;
    }

    @JsonIgnore
    public String getId() {
        return id;
    }

    public boolean hasAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    @JsonIgnore
    public String getName() {
        return this.userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
