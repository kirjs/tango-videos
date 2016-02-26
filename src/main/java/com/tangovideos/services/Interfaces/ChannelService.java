package com.tangovideos.services.Interfaces;

import com.tangovideos.models.Channel;
import org.neo4j.graphdb.Node;

import java.util.List;

public interface ChannelService {
    Node addChannel(Channel channel);

    List<Channel> list();
}
