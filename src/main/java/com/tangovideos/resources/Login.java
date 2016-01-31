package com.tangovideos.resources;

import com.tangovideos.models.UserProfile;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("/api/login")
@Produces(MediaType.TEXT_HTML)
public class Login {
    @GET
    public String goGet() {
        return "<form name=\"loginform\" action=\"\" method=\"POST\" accept-charset=\"UTF-8\" role=\"form\">\n" +
                "    <input class=\"form-control\" placeholder=\"Username or Email\" name=\"username\" type=\"text\">\n" +
                "    <input class=\"form-control\" placeholder=\"Password\" name=\"password\" type=\"password\" value=\"\">\n" +
                "    <input name=\"rememberMe\" type=\"checkbox\" value=\"true\"> Remember Me\n" +
                "    <input class=\"btn btn-lg btn-success btn-block\" type=\"submit\" value=\"Login\">\n" +
                "</form>\n";
    }

    @POST
    public Response doPost(@FormParam("username") String username,
                           @FormParam("password") String password,
                           @FormParam("rememberMe") @DefaultValue("false") Boolean rememberMe) {
        Subject currentUser = SecurityUtils.getSubject();

        UsernamePasswordToken token =
                new UsernamePasswordToken(username, password);
        token.setRememberMe(rememberMe == null ? false : rememberMe);
        try {
            currentUser.login(token);
        } catch (Exception uae) {
            return Response.status(401).
                    entity(uae).
                    type(MediaType.APPLICATION_JSON).
                    build();
        }

        return Response.status(200).
                entity(new UserProfile()).
                type(MediaType.APPLICATION_JSON).
                build();
    }
}
