package com.tangovideos.services.Interfaces;

import org.neo4j.graphdb.Node;

import java.util.Set;

public interface DancerService {
    Node insertOrGet(String dancerId);

    Set<String> getForVideo(String videoId);
}
