package com.tangovideos.resources;

import com.tangovideos.responses.VideosResponse;
import com.tangovideos.services.TangoVideosServiceFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("api/videos")
@Produces(MediaType.APPLICATION_JSON)
public class VideoResource {


    @GET
    @RequiresPermissions("videos:read")
    public String getVideos() {
        return "{\"a\": \"list\"}";
    }


    @GET
    @Path("list")
    public Response list() {
        final VideosResponse videos = new VideosResponse();
        videos.setList(TangoVideosServiceFactory.getVideoService().list());

        return Response.status(200)
                .entity(videos.toJson())
                .type(MediaType.APPLICATION_JSON)
                .build();
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
