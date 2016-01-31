package com.tangovideos.resources;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("/login")
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
    public String doPost(@FormParam("username") String username,
                         @FormParam("password") String password,
                         @FormParam("rememberMe") @DefaultValue("false") Boolean rememberMe) {
        Subject currentUser = SecurityUtils.getSubject();

        UsernamePasswordToken token =
                new UsernamePasswordToken(username, password);
        token.setRememberMe(rememberMe == null ? false : rememberMe);
        try {
            currentUser.login(token);
        } catch (UnknownAccountException uae) {
            return "Don't know such account";
        } catch (IncorrectCredentialsException ice) {
            return "Wrong password";
        } catch (LockedAccountException lae) {
            return "Account locked";
        } catch (ExcessiveAttemptsException eae) {
            return "Too many attempts";
        } catch (AuthenticationException ae) {
            return "Something is wrong";
        }

        return "You're logged in";
    }
}
