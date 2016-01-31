package com.tangovideos.resources;

import com.google.inject.Inject;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("api/videos")
@Produces(MediaType.APPLICATION_JSON)
public class Video {



    @GET
    @RequiresPermissions("videos:read")
    public String getVideos() {
        return "{\"a\": \"list\"}";
    }

    @GET
    @Path("{id}")
    public String getVideo(@PathParam("id") String id) {
        return "{\"a\": \"" + id + "\"}";
    }

    @POST
    @Path("{id}")
    public String addVideo(@FormParam("id") String id) {

        return "{\"a\": \"" + id + "\"}";
    }
}
