package dk.rohdef.hfserver.jersey.security;

import dk.rohdef.hfserver.model.LoginSession;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * Created by Rohde Fischer on 7/30/15.
 */
public class HfSecurityContext implements SecurityContext {
    private LoginSession loginSession;

    public HfSecurityContext(LoginSession loginSession) {
        this.loginSession = loginSession;
    }

    @Override
    public Principal getUserPrincipal() {
        if (loginSession != null && !loginSession.hasAccess())
            return null;
        return loginSession;
    }

    @Override
    public boolean isUserInRole(String role) {
        if (loginSession == null) return false;

        return loginSession.getUserType().name().equalsIgnoreCase(role);
    }

    @Override
    public boolean isSecure() {
        if (loginSession == null) return false;

        return loginSession.hasAccess();
    }

    @Override
    public String getAuthenticationScheme(){
        return this.BASIC_AUTH;
    }
}