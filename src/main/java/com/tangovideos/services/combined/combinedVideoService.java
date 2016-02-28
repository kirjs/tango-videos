package com.tangovideos.services.combined;

import com.tangovideos.models.Channel;
import com.tangovideos.models.Video;
import com.tangovideos.services.Interfaces.ChannelService;
import com.tangovideos.services.Interfaces.DancerService;
import com.tangovideos.services.Interfaces.VideoService;
import com.tangovideos.services.YoutubeService;
import org.neo4j.graphdb.Node;

public class CombinedVideoService {
    final DancerService dancerService;
    final VideoService videoService;
    final ChannelService channelService;
    final YoutubeService youtubeService;

    public CombinedVideoService(DancerService dancerService,
                                VideoService videoService,
                                ChannelService channelService,
                                YoutubeService youtubeService) {
        this.dancerService = dancerService;
        this.videoService = videoService;
        this.channelService = channelService;
        this.youtubeService = youtubeService;

    }


    public void addVideo(Video video) {
        final Node videoNode = videoService.addVideo(video);
        video.getDancers().stream()
                .map(dancerService::insertOrGetNode)
                .forEach(d -> dancerService.addToVideo(d, videoNode));

        final String channelId = video.getChannelId();
        if (!channelService.exists(channelId)) {
            final Channel channel = youtubeService.getChannelInfoById(channelId);
            channelService.addChannel(channel);
        }

        channelService.addVideoToChannel(video.getId(), channelId);
    }

    public boolean videoExists(String videoId) {
        return videoService.exists(videoId);
    }
}
