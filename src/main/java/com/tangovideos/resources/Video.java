package com.tangovideos.resources;

import com.google.inject.Inject;
import com.tangovideos.services.VideosService;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("videos")
@Produces(MediaType.APPLICATION_JSON)
public class Video {

    VideosService service = VideosService.getInstance();

    @GET
    @RequiresPermissions("videos:read")
    public String getVideos() {
        return "{\"a\": \"list\"}";
    }

    @GET @Path("{id}")
    public String getVideo(@PathParam("id") String id) {
        return "{\"a\": \""+id+"\"}";
    }
}
