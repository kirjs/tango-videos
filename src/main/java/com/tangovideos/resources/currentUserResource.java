package com.tangovideos.resources;

import com.tangovideos.models.UserNotLoggedIn;
import com.tangovideos.services.TangoVideosServiceFactory;
import org.apache.shiro.subject.Subject;
import org.secnod.shiro.jaxrs.Auth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("api/currentUser")
@Produces(MediaType.APPLICATION_JSON)
public class CurrentUserResource {

    @GET
    public Response getCurrentUser(@Auth Subject subject) {
        Object data = subject.isAuthenticated() ?
                TangoVideosServiceFactory.getUserService().getUserProfile(subject.getPrincipal().toString()) :
                new UserNotLoggedIn();

        return Response.status(200).
                entity(data).
                type(MediaType.APPLICATION_JSON).
                build();
    }

}
