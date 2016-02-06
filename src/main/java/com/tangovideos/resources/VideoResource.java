package com.tangovideos.resources;

import com.google.common.collect.ImmutableSet;
import com.tangovideos.services.Interfaces.DancerService;
import com.tangovideos.services.TangoVideosServiceFactory;
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
    final DancerService dancerService = TangoVideosServiceFactory.getDancerService();

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
    @Path("exist/{ids}")
    public Response exist(@PathParam("ids") String ids) {
        final ImmutableSet<String> idSet = ImmutableSet.copyOf(ids.split(","));
        final String result = new JSONArray(TangoVideosServiceFactory.getVideoService().exist(idSet)).toString();

        return Response.status(200)
                .entity(result)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @POST
    @Path("{id}/dancers/add")
    public Response addDancer(@PathParam("id") String id, @FormParam("name") String dancerId) {
        final Node dancer = dancerService.insertOrGetNode(dancerId);
        final Node video = TangoVideosServiceFactory.getVideoService().get(id);
        TangoVideosServiceFactory.getDancerService().addToVideo(dancer, video);
        final Set<String> entity = TangoVideosServiceFactory.getDancerService().getForVideo(id);
        final String result = new JSONArray(entity).toString();
        return Response.status(200)
                .entity(result)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @POST
    @Path("{id}/songs/update")
    public Response updateSong(@PathParam("index") String index, @FormParam("field") String field, @FormParam("data") String data) {
//        final Node dancer = dancerService.insertOrGetNode(dancerId);
//        final Node video = TangoVideosServiceFactory.getVideoService().get(id);
//        TangoVideosServiceFactory.getDancerService().addToVideo(dancer, video);
//        final Set<String> entity = TangoVideosServiceFactory.getDancerService().getForVideo(id);
//        final String result = new JSONArray(entity).toString();
        return Response.status(200)
                .entity("TODO")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @POST
    @Path("{id}/dancers/remove")
    public Response removeDancer(@PathParam("id") String id, @FormParam("name") String dancerId) {
        final Node dancer = dancerService.insertOrGetNode(dancerId);
        final Node video = TangoVideosServiceFactory.getVideoService().get(id);
        dancerService.removeFromVideo(dancer, video);
        final Set<String> entity = dancerService.getForVideo(id);
        final String result = new JSONArray(entity).toString();
        return Response.status(200)
                .entity(result)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
