package com.tangovideos.services.neo4j;

import com.tangovideos.data.Labels;
import com.tangovideos.data.Relationships;
import com.tangovideos.models.Video;
import com.tangovideos.services.Interfaces.UserService;
import com.tangovideos.services.Interfaces.VideoService;
import com.tangovideos.services.YoutubeService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;


public class Neo4jVideoService implements VideoService {
    private final GraphDatabaseService graphDb;
    private final UserService userService;
    private final YoutubeService youtubeService;

    public Neo4jVideoService(GraphDatabaseService graphDb, UserService userService, YoutubeService youtubeService) {
        this.graphDb = graphDb;
        this.userService = userService;
        this.youtubeService = youtubeService;
        fillTheDb();
    }


    private void fillTheDb() {
        try (Transaction tx = this.graphDb.beginTx()) {
            tx.success();
        }
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
}
