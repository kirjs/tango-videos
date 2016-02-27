package com.tangovideos.resources;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.tangovideos.models.Song;
import com.tangovideos.models.Video;
import com.tangovideos.resources.inputs.*;
import com.tangovideos.services.Interfaces.ChannelService;
import com.tangovideos.services.Interfaces.DancerService;
import com.tangovideos.services.Interfaces.VideoService;
import com.tangovideos.services.TangoVideosServiceFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
    final DancerService dancerService;
    final VideoService videoService;
    final ChannelService channelService;

    // TODO: this is used for testing, but it would be nice to use some dependency injection mechanism.
    public VideoResource(DancerService dancerService, VideoService videoService, ChannelService channelService){
        this.dancerService = dancerService;
        this.videoService = videoService;
        this.channelService = channelService;
    }

    public  VideoResource(){
        dancerService = TangoVideosServiceFactory.getDancerService();
        videoService = TangoVideosServiceFactory.getVideoService();
        channelService = TangoVideosServiceFactory.getChannelService();

    }

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
    public Response needsreview(
            @QueryParam("dancers") Boolean dancers,
            @QueryParam("song") Boolean song,
            @QueryParam("orquestra") Boolean orquestra,
            @QueryParam("genre") Boolean genre,
            @QueryParam("year") Boolean year
    ) {
        final String result = new JSONArray(videoService.needsReview(
                ImmutableMap.of("dancers", dancers,
                        "songname", song,
                        "orquestra", orquestra,
                        "genre", genre,
                        "year", year)
        )).toString();

        return Response.status(200)
                .entity(result)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("add")
    public Response add(JustId id) {
        final Video videoInfo = TangoVideosServiceFactory.getYoutubeService().getVideoInfo(id.getId());
        add(videoInfo);
        return Response.status(200)
                .entity("true")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    public void add(Video video) {
        final Node videoNode = videoService.addVideo(video);
        video.getDancers().stream()
                .map(dancerService::insertOrGetNode)
                .forEach(d -> dancerService.addToVideo(d, videoNode));
        channelService.addVideoToChannel(video.getId(), video.getChannelId());
    }

    @POST
    @Path("{id}/hide")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response hide(@PathParam("id") String id, JustValue value) {
        videoService.hide(id, value.getValue());

        return Response.status(200)
                .entity("true")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @POST
    @Path("{id}/markComplete")
    @Consumes(MediaType.APPLICATION_JSON)
    @RequiresPermissions("video:write")
    public Response markComplete(@PathParam("id") String id, JustValue payload) {
        videoService.markComplete(id, payload.getValue());

        return Response.status(200)
                .entity(payload.getValue().toString())
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}/dancers/add")
    public Response addDancer(@PathParam("id") String id, JustName dancerName) {
        final Node dancer = dancerService.insertOrGetNode(dancerName.getName());
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
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateSong(
            @PathParam("id") String id,
            SongUpdate songUpdate
    ) {
        final Song song = TangoVideosServiceFactory.getSongService()
                .updateField(id, songUpdate.getIndex(), songUpdate.getField(), songUpdate.getData());

        return Response.status(200)
                .entity(new JSONObject(song).toString())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}/update")
    public Response updateField(@PathParam("id") String id, VideoUpdate payload) {
        this.videoService.updateField(id, payload.getField(), payload.getValue());

        return Response.status(200)
                .entity("true")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}/dancers/remove")
    public Response removeDancer(@PathParam("id") String id, JustName dancer) {
        final Node dancerNode = dancerService.insertOrGetNode(dancer.getName());
        final Node video = TangoVideosServiceFactory.getVideoService().get(id);
        dancerService.removeFromVideo(dancerNode, video);
        final Set<String> entity = dancerService.getForVideo(id);
        final String result = new JSONArray(entity).toString();
        return Response.status(200)
                .entity(result)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
