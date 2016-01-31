package com.tangovideos.resources;

import com.tangovideos.services.Interfaces.UserService;
import com.tangovideos.services.neo4j.TangoVideosServiceFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("/")
@Produces(MediaType.TEXT_HTML)
public class Index {
    @GET
    public String getIndex() {

        final UserService userService = TangoVideosServiceFactory.getUserService();
        userService.list();
        return "<a href = /videos>videos</a>";
    }
}
