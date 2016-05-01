package com.tangovideos.resources;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.FileNotFoundException;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("/")
@Produces(MediaType.TEXT_HTML)
public class pingResource {
    @GET
    @Path("/ping")
    public String getBundle(@Context ServletContext context) throws FileNotFoundException {
        return "pong";
    }
}
