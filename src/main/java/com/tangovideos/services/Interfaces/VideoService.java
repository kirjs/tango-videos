package com.tangovideos.services.Interfaces;

import org.neo4j.graphdb.Node;

import java.util.List;
import java.util.Map;

public interface VideoService {
    boolean videoExistis(String videoId);

    void addVideo(String videoId, Node user);

    List<Map<String, String>> list();
}
