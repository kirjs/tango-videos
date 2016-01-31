package com.tangovideos.resources;

import com.tangovideos.models.ErrorMessage;
import com.tangovideos.models.UserNotLoggedIn;
import com.tangovideos.models.UserProfile;
import com.tangovideos.services.VideosService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.secnod.shiro.jaxrs.Auth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("api/currentUser")
@Produces(MediaType.APPLICATION_JSON)
public class currentUser {

    @GET
    public Response getCurrentUser(@Auth Subject subject) {
        Object data = subject.isAuthenticated() ? new UserProfile() : new UserNotLoggedIn();

        return Response.status(200).
                entity(data).
                type(MediaType.APPLICATION_JSON).
                build();
    }

}
