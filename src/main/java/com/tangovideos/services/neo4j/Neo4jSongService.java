package com.tangovideos.services.neo4j;

import com.tangovideos.models.Song;
import com.tangovideos.services.Interfaces.SongService;
import org.neo4j.graphdb.GraphDatabaseService;

public class Neo4jSongService implements SongService {
    final private GraphDatabaseService graphDb;

    public Neo4jSongService(GraphDatabaseService graphDb) {

        this.graphDb = graphDb;
    }

    @Override
    public Song insert(String videoId, Integer index, String field, String value) {
        return null;
    }
}
