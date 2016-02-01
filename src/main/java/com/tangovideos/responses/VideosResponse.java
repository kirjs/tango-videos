package com.tangovideos.responses;


import org.json.JSONArray;
import org.json.JSONObject;
import org.neo4j.cypher.internal.compiler.v2_2.functions.Str;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@XmlRootElement
public class VideosResponse {
    public void setList(List<Map<String, String>> list) {
        this.list = list;
    }

    @XmlElement(name = "videos")
    List<Map<String, String>> list;

    public String toJson(){
        return new JSONArray(list).toString();
    }


}