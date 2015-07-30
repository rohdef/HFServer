package dk.rohdef.hfserver.model;

/**
 * Created by rohdef on 7/13/15.
 */
public class LoginRequest {
    private String userName, password;

    public LoginRequest() {
    }

    public LoginRequest(String userName, String password) {
        this();
        this.userName = userName;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
