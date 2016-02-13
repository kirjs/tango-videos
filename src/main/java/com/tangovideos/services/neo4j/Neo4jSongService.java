package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableMap;
import com.tangovideos.models.Song;
import com.tangovideos.services.Interfaces.SongService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

public class Neo4jSongService implements SongService {
    final private GraphDatabaseService graphDb;

    public Neo4jSongService(GraphDatabaseService graphDb) {

        this.graphDb = graphDb;
    }

    @Override
    public Song updateField(String videoId, Integer index, String field, String value) {
        try (Transaction tx = graphDb.beginTx()) {

            String query = "MATCH (v:Video {id: {videoId}}) " +
                    "MERGE v<-[:PLAYS_IN{index: {index}}]-(s:Song {year: {year}}) " +
                    "RETURN s as song";

            final ImmutableMap<String, Object> parameters = ImmutableMap.of(
                    "videoId", videoId,
                    "index", index,
                    "year", value);

            graphDb.execute(query, parameters);
            tx.success();
        }
        return new Song();
    }
}
