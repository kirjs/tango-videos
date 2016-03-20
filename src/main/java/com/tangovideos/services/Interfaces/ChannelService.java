package com.tangovideos.services.Interfaces;

import com.tangovideos.models.Channel;
import com.tangovideos.services.YoutubeService;
import com.tangovideos.services.combined._CombinedVideoService;
import org.neo4j.graphdb.Node;

import java.util.List;
import java.util.Set;

public interface ChannelService {
    Node addChannel(Channel channel);

    List<Channel> list();

    Channel get(String id);

    void update(Channel channel);

    boolean addVideoToChannel(String videoId, String channelId);

    long fetchAllVideos(YoutubeService youtubeService, _CombinedVideoService combinedVideoService, String channelId);

    boolean exists(String channelId);

    void setAutoupdate(String id, boolean b);

    Set<String> getAutoupdatedChannelIds();
}
