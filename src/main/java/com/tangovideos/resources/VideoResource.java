package com.tangovideos.resources;

import com.tangovideos.services.TangoVideosServiceFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONArray;
import org.neo4j.graphdb.Node;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

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

        final String result = new JSONArray(TangoVideosServiceFactory.getVideoService().list()).toString();

        return Response.status(200)
                .entity(result)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path("{id}")
    public String getVideo(@PathParam("id") String id) {
        return "{\"a\": \"" + id + "\"}";
    }

    @POST
    @Path("{id}/dancers/add")
    public Response addVideo(@PathParam("id") String id, @FormParam("name") String dancerId) {
        final Node dancer = TangoVideosServiceFactory.getDancerService().insertOrGetNode(dancerId);
        final Node video = TangoVideosServiceFactory.getVideoService().get(id);
        TangoVideosServiceFactory.getDancerService().addToVideo(video, dancer);
        final Set<String> entity = TangoVideosServiceFactory.getDancerService().getForVideo(id);
        final String result = new JSONArray(entity).toString();
        return Response.status(200)
                .entity(result)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
