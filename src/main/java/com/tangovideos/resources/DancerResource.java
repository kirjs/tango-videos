package com.tangovideos.resources;

import com.tangovideos.models.Dancer;
import com.tangovideos.resources.inputs.JustName;
import com.tangovideos.services.Interfaces.DancerService;
import com.tangovideos.services.Interfaces.VideoService;
import com.tangovideos.services.TangoVideosServiceFactory;
import com.tangovideos.services.neo4j.Neo4jDancerService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("api/dancers")
@Produces(MediaType.APPLICATION_JSON)
public class DancerResource {

    private DancerService dancerService = TangoVideosServiceFactory.getDancerService();
    private VideoService videoService = TangoVideosServiceFactory.getVideoService();

    @GET
    @Path("listNames")
    public Response listNames() {
        return Response.status(200)
                .entity(new JSONArray(dancerService.list()).toString())
                .type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("list")
    public Response list() {
        return Response.status(200)
                .entity(new JSONArray(dancerService.list(0, 1000, Neo4jDancerService.SortBy.VIDEO_COUNT)).toString())
                .type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("{id}")
    public Response getVideo(@PathParam("id") String id) {
        final Dancer dancer = TangoVideosServiceFactory.getDancerService().get(id);
        dancer.setVideos(videoService.listByDancer(id));

        return Response.status(200)
                .entity(new JSONObject(dancer).toString())
                .type(MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("{id}/addPseudonym")
    public Response addPseudonym(@PathParam("id") String id, JustName payload) {
        final Dancer dancer = TangoVideosServiceFactory.getDancerService().addPseudonym(id, payload.getName());
        dancer.setVideos(videoService.listByDancer(id));

        return Response.status(200)
                .entity(new JSONObject(dancer).toString())
                .type(MediaType.APPLICATION_JSON).build();
    }
}
