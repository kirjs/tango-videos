package com.tangovideos.resources;

import com.tangovideos.models.Dancer;
import com.tangovideos.services.Interfaces.DancerService;
import com.tangovideos.services.Interfaces.VideoService;
import com.tangovideos.services.TangoVideosServiceFactory;
import org.json.JSONArray;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("api/dancers")
@Produces(MediaType.APPLICATION_JSON)
public class DancerResource {

    private DancerService dancerService =TangoVideosServiceFactory.getDancerService();
    private VideoService videoService =TangoVideosServiceFactory.getVideoService();

    @GET
    @Path("list")
    public Response list() {
        return Response.status(200)
                .entity(new JSONArray(dancerService.list()).toString())
                .type(MediaType.APPLICATION_JSON).build();
    }



    @GET
    @Path("{id}")
    public Response getVideo(@PathParam("id") String id) {
        final Dancer dancer = TangoVideosServiceFactory.getDancerService().get(id);
        dancer.setVideos(videoService.listByDancer(id));

        return Response.status(200)
                .entity(dancer)
                .type(MediaType.APPLICATION_JSON).build();
    }
}
