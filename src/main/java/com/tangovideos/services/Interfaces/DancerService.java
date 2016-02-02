package com.tangovideos.services.Interfaces;

import org.neo4j.graphdb.Node;

import java.util.Set;

public interface DancerService {
    Node getNode(String dancerId);

    Set<String> getForVideo(String videoId);
}
