package com.tangovideos.data;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;

public enum Labels {
    USER(DynamicLabel.label("User")),
    ROLE(DynamicLabel.label("Role")),
    PERMISSION(DynamicLabel.label("Permission"));
    public final Label label;

    public Label valueOf(){
        return label;
    }
    Labels(Label label) {
        this.label = label;
    }
}
