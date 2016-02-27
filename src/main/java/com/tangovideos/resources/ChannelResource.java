package com.tangovideos.resources;

import com.tangovideos.models.Channel;
import com.tangovideos.resources.inputs.JustId;
import com.tangovideos.services.Interfaces.ChannelService;
import com.tangovideos.services.TangoVideosServiceFactory;
import com.tangovideos.services.YoutubeService;
import com.tangovideos.services.combined.CombinedVideoService;
import org.json.JSONArray;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("api/channels")
@Produces(MediaType.APPLICATION_JSON)
public class ChannelResource {

    private ChannelService channelService = TangoVideosServiceFactory.getChannelService();
    private YoutubeService youtubeService = TangoVideosServiceFactory.getYoutubeService();
    private CombinedVideoService combinedVideoService = TangoVideosServiceFactory.getCombinedVideoService();


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("add")
    public Response addChannel(JustId payload) {
        final Channel channel = youtubeService.getChannelInfoById(payload.getId());

        channelService.addChannel(channel);

        return list();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("fetch")
    public Response fetchLatestVideos(JustId payload) {
        channelService.fetchAllVideos(youtubeService, combinedVideoService, payload.getId());
        return list();
    }

    @GET
    @Path("list")
    public Response list() {
        return Response.status(200)
                .entity(new JSONArray(channelService.list()).toString())
                .type(MediaType.APPLICATION_JSON).build();
    }

}
