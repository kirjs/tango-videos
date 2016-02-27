package com.tangovideos.services.Interfaces;

import com.tangovideos.models.Channel;
import com.tangovideos.services.YoutubeService;
import com.tangovideos.services.combined.CombinedVideoService;
import org.neo4j.graphdb.Node;

import java.util.List;

public interface ChannelService {
    Node addChannel(Channel channel);

    List<Channel> list();

    Channel get(String id);

    void update(Channel channel);

    boolean addVideoToChannel(String videoId, String channelId);

    long fetchAllVideos(YoutubeService youtubeService, CombinedVideoService combinedVideoService, String channelId);

    boolean exists(String channelId);
}
