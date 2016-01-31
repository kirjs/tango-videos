package com.tangovideos.resources;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("hi")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt(@Context SecurityContext sc) {
        sc.isUserInRole("test");


        return "Got it!";
    }
}
