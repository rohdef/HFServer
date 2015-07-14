package dk.rohdef.hfserver.rest;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import dk.rohdef.hfserver.model.LoginRequest;
import dk.rohdef.hfserver.model.LoginSession;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.UUID;

/**
 * Created by rohdef on 7/13/15.
 */
@Api(value = "/login", description = "A simple mock login service, will be replaces with a proper OAuth service")
@Path("/login")
public class LoginService extends BaseService {
    @POST
    @ApiOperation(value = "Login method",
            position = 1)
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
    public LoginSession login(LoginRequest loginRequest) {
        LoginSession loginSession = null;
        boolean successful = true;

        if (successful) {
            loginSession = new LoginSession();
            loginSession.setUsername(loginRequest.getUsername());
            loginSession.setTime(new Date());
            loginSession.setToken(UUID.randomUUID());
        }

        return loginSession;
    }
}
