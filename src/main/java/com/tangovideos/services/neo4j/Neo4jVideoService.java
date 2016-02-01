package com.tangovideos.services.neo4j;

import com.google.api.client.util.Lists;
import com.tangovideos.data.Labels;
import com.tangovideos.data.Relationships;
import com.tangovideos.models.Video;
import com.tangovideos.services.Interfaces.UserService;
import com.tangovideos.services.Interfaces.VideoService;
import com.tangovideos.services.YoutubeService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Neo4jVideoService implements VideoService {
    private final GraphDatabaseService graphDb;
    private final UserService userService;
    private final YoutubeService youtubeService;

    public Neo4jVideoService(GraphDatabaseService graphDb, UserService userService, YoutubeService youtubeService) {
        this.graphDb = graphDb;
        this.userService = userService;
        this.youtubeService = youtubeService;
    }



    @Override
    public boolean videoExistis(String videoId) {
        return this.graphDb.findNodes(Labels.VIDEO.label, "id", videoId).hasNext();
    }

    @Override
    public void addVideo(String videoId, Node user) {
        if(videoExistis(videoId)){
            throw new RuntimeException("VideoExists");
        }

        final Video videoInfo = youtubeService.getVideoInfo(videoId);

        if(videoInfo == null){
            throw new RuntimeException("VideoDoesNotExistOnYoutube");
        }

        final Node node = graphDb.createNode(Labels.VIDEO.label);
        node.setProperty("title", videoInfo.getTitle());
        node.setProperty("publishedAt", videoInfo.getPublishedAt());
        node.setProperty("id", videoInfo.getId());
        user.createRelationshipTo(node, Relationships.ADDED);
    }

    @Override
    public List<Map<String, String>> list() {
        List<Map<String, String>> videos = Lists.newArrayList();
        try (Transaction tx = this.graphDb.beginTx()) {
            final ResourceIterator<Node> nodes = this.graphDb.findNodes(Labels.VIDEO.label);

            while(nodes.hasNext()){
                final Map<String, String> allProperties = nodes.next().getAllProperties()
                        .entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));

                videos.add(allProperties);
            }
            tx.success();
        }
        return videos;
    }
}
