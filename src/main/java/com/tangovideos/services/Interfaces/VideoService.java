package com.tangovideos.services.Interfaces;

import com.tangovideos.models.Video;

import org.neo4j.graphdb.Node;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface VideoService {
    boolean exists(String videoId);

    Node addVideo(@NotNull Video video);

    Node get(String videoId);

    List<Video> list();
    List<Video> list(int skip, int limit);
    boolean hide(String id, Boolean value);

    List<Video> listByDancer(String dancerId);

    Set<String> exist(Set<String> ids);

    List<Video> needsReview(Map<String, Boolean> of);

    void updateField(String id, String field, String value);

    void markComplete(String id, Boolean value);
}
