package com.tangovideos.services.Interfaces;

import com.tangovideos.models.Channel;
import com.tangovideos.services.YoutubeService;
import org.neo4j.graphdb.Node;

import java.util.List;

public interface ChannelService {
    Node addChannel(Channel channel);

    List<Channel> list();

    Channel get(String id);

    void update(Channel channel);

    boolean addVideoToChannel(String videoId, String channelId);

    long fetchAllVideos(YoutubeService youtubeService, VideoService videoService, String channelId);

    boolean exists(String channelId);
}
