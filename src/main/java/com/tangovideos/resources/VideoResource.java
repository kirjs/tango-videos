package com.tangovideos.resources;

import com.google.common.collect.ImmutableSet;
import com.tangovideos.models.Song;
import com.tangovideos.models.Video;
import com.tangovideos.services.Interfaces.DancerService;
import com.tangovideos.services.Interfaces.VideoService;
import com.tangovideos.services.TangoVideosServiceFactory;
import org.json.JSONArray;
import org.json.JSONObject;
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
    final VideoService videoService = TangoVideosServiceFactory.getVideoService();

    @GET
    @Path("list")
    public Response list() {
        final String result = new JSONArray(videoService.list()).toString();
        return Response.status(200)
                .entity(result)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path("list/{skip}/{limit}")
    public Response listPaged(@PathParam("skip") int skip, @PathParam("limit") int limit) {
        final String result = new JSONArray(videoService.list(skip, limit)).toString();

        return Response.status(200)
                .entity(result)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path("needsreview")
    public Response needsreview() {
        final String result = new JSONArray(videoService.needsReview()).toString();

        return Response.status(200)
                .entity(result)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @POST
    @Path("add")
    public Response add(@FormParam("id") String id) {
        final Video videoInfo = TangoVideosServiceFactory.getYoutubeService().getVideoInfo(id);
        final Node video = videoService.addVideo(videoInfo);
        videoInfo.getDancers().stream()
                .map(dancerService::insertOrGetNode)
                .forEach(d -> dancerService.addToVideo(d, video));

        return Response.status(200)
                .entity("true")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @POST
    @Path("hide")
    public Response hide(@FormParam("id") String id) {
        videoService.hide(id);

        return Response.status(200)
                .entity("true")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
    @POST
    @Path("{id}/markComplete")
    public Response markComplete(@PathParam("id") String id, @FormParam("value") Boolean value) {
        videoService.markComplete(id, value);

        return Response.status(200)
                .entity("true")
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
    public Response updateSong(
            @PathParam("id") String id,
            @FormParam("index") Integer index,
            @FormParam("field") String field,
            @FormParam("data") String data
    ) {
        final Song song = TangoVideosServiceFactory.getSongService().updateField(id, index, field, data);

        return Response.status(200)
                .entity(new JSONObject(song).toString())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @POST
    @Path("{id}/update")
    public Response updateField(
            @PathParam("id") String id,
            @FormParam("field") String field,
            @FormParam("value") String value
    ) {
        this.videoService.updateField(id, field, value);

        return Response.status(200)
                .entity("true")
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
