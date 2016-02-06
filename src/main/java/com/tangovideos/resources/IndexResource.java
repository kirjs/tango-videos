package com.tangovideos.resources;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("/")
@Produces(MediaType.TEXT_HTML)
public class IndexResource {
    @GET
    public InputStream getIndex(@Context ServletContext context) throws FileNotFoundException {
        return getResource("static/index.html");
    }

    private InputStream getResource(String file) throws FileNotFoundException {
        return IndexResource.class.getResourceAsStream(file);
    }

    @GET
    @Path("/bundle.js")
    public InputStream getBundle(@Context ServletContext context) throws FileNotFoundException {
        return getResource("static/bundle.js");
    }

    @GET
    @Path("/vendor.bundle.js")
    public InputStream getVendorBundle(@Context ServletContext context) throws FileNotFoundException {
        return getResource("static/vendor.bundle.js");
    }
}
