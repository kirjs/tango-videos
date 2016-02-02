package com.tangovideos.resources;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.File;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("/")
@Produces(MediaType.TEXT_HTML)
public class IndexResource {
    @GET
    public File getIndex(@Context ServletContext context) {
        final String path = context.getRealPath("static/index.html");


        return new File(path).getAbsoluteFile();
    }
}
