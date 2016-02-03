package com.tangovideos.services.Interfaces;

import com.tangovideos.models.Dancer;
import org.neo4j.graphdb.Node;

import java.util.List;
import java.util.Set;

public interface DancerService {
    List<Dancer> list();

    void addToVideo(Node dancer, Node video);
    void removeFromVideo(Node dancer, Node video);

    Node insertOrGetNode(String dancerId);

    Set<String> getForVideo(String videoId);

    Dancer get(String id);
}
