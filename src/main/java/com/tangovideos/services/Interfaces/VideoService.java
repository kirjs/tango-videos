package com.tangovideos.services.Interfaces;

import com.tangovideos.models.VideoResponse;
import org.neo4j.graphdb.Node;

import java.util.List;
import java.util.Set;

public interface VideoService {
    boolean videoExists(String videoId);

    void addVideo(String videoId, Node user);
    Node get(String videoId);

    List<VideoResponse> list();

    Set<String> addDancer(String id, String performer);

}
