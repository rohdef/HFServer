package dk.rohdef.hfserver.model;

/**
 * Created by rohdef on 7/13/15.
 */
public class LoginRequest {
    private String username, password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
