package com.tangovideos.models;

import org.neo4j.cypher.internal.compiler.v2_2.functions.Str;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;

@XmlRootElement
public class UserProfile {
    public Boolean getAuthentificated() {
        return authentificated;
    }

    public void setAuthentificated(Boolean authentificated) {
        this.authentificated = authentificated;
    }

    @XmlElement
    Boolean authentificated = true;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElement
    String id;

    public HashSet<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(HashSet<String> permissions) {
        this.permissions = permissions;
    }

    HashSet<String> permissions;

}
