package com.tangovideos.services.Interfaces;

import com.tangovideos.models.Channel;
import com.tangovideos.services.YoutubeService;
import org.neo4j.graphdb.Node;

import java.util.List;

public interface ChannelService {
    Node addChannel(Channel channel);

    List<Channel> list();

    Channel get(String id);

    int update(YoutubeService youtubeService, String channelId);
}
