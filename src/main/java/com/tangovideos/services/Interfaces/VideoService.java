package com.tangovideos.services.Interfaces;

import com.tangovideos.models.Video;
import com.tangovideos.models.VideoResponse;
import org.neo4j.graphdb.Node;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

public interface VideoService {
    boolean exists(String videoId);

    Node addVideo(@NotNull Video video);

    Node get(String videoId);

    List<VideoResponse> list();
    List<VideoResponse> list(int skip, int limit);
    boolean hide(String id);

    List<VideoResponse> listByDancer(String dancerId);

    Set<String> exist(Set<String> ids);

    List<VideoResponse> needsReview();

    void updateField(String id, String field, String value);
}
