package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableSet;
import com.tangovideos.models.Song;
import com.tangovideos.models.Video;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import java.util.HashSet;

public class TestHelpers {
    public static int videoIndex = 0;
    private static Neo4jSongService neo4jSongService;

    public static void reset(){
        videoIndex = 0;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public static Node setUpAdminNode(GraphDatabaseService graphDb) {
        Node node;
        try (Transaction tx = graphDb.beginTx()) {
            final HashSet<Node> nodes =
                    new Neo4jPermissionsService(graphDb).addPermissions(ImmutableSet.of("video:read", "video:write"));

            final Node adminRole = new Neo4JRoleService(graphDb).addRole("admin", nodes);

            node = new Neo4jUserService(graphDb).addUser("admin", "hashme", adminRole);
            tx.success();
        }
        return node;
    }

    public static Node addVideo(GraphDatabaseService graphDb, String videoId){
        return addVideo(graphDb, videoId, 0);
    }

    public static void addSongToVideo(GraphDatabaseService graphDb, String videoId, Song song) {
        neo4jSongService = new Neo4jSongService(graphDb);
        neo4jSongService.updateField(videoId, 0, "name", song.getName());
        neo4jSongService.updateField(videoId, 0, "year", song.getYear());
    }

    public static void addVideoAndDancer(GraphDatabaseService graphDb, String videoId, String dancerId) {
        final Neo4jDancerService neo4jDancerService = new Neo4jDancerService(graphDb);
        final Node video = addVideo(graphDb, videoId, videoIndex++);

        neo4jDancerService.addToVideo(
                neo4jDancerService.insertOrGetNode(dancerId),
                video
        );
    }


    public static Video generateFakeVideo(String id) {
        Video video = new Video(id, "title", "publishedAt");
        video.setDescription("Description");
        video.setDancers(ImmutableSet.of());
        return video;
    }

    private static Node addVideo(GraphDatabaseService graphDb, String videoId, int i) {
        final Video video = new Video(videoId, "Title", "Date");

            video.setDescription("Description");
        final Node videoNode = new Neo4jVideoService(graphDb).addVideo(video);
        try(Transaction tx = graphDb.beginTx()){
            videoNode.setProperty("addedAt", videoNode.getProperty("addedAt") + String.valueOf(i));
            tx.success();
        }
        return videoNode;
    }
}
