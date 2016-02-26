package com.tangovideos.services.Interfaces;
import org.neo4j.graphdb.Node;

public interface ChannelService {
    Node addChannel(String channelId);
}
