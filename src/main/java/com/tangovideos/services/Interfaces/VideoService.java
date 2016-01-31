package com.tangovideos.services.Interfaces;

import org.neo4j.graphdb.Node;

public interface VideoService {
    void addVideo(String videoId, String title, String image, Node admin);
}
