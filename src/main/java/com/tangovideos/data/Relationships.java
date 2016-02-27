package com.tangovideos.data;

import org.neo4j.graphdb.RelationshipType;

public enum Relationships implements RelationshipType
{
    IS,
    ADDED,
    DANCES, IS_ON, CAN
}
