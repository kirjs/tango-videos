package com.tangovideos.services.Interfaces;

import org.neo4j.graphdb.Node;

public interface VideoService {
    boolean videoExistis(String videoId);

    void addVideo(String videoId, Node user);
}
