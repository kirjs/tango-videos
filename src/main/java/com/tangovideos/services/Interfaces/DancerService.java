package com.tangovideos.services.Interfaces;

import com.tangovideos.models.Dancer;
import com.tangovideos.services.neo4j.Neo4jDancerService;
import org.neo4j.graphdb.Node;

import java.util.List;
import java.util.Set;

public interface DancerService {
    List<Dancer> list();

    List<Dancer> list(int skip, int limit, Neo4jDancerService.SortBy sortBy);

    void addToVideo(Node dancer, Node video);

    Set<Set<String>> getaAllDancersByVideo();

    void removeFromVideo(Node dancer, Node video);

    Node insertOrGetNode(String dancerId);

    Set<String> getForVideo(String videoId);

    Dancer get(String id);

    Dancer addPseudonym(String id, String name);
}
