package com.tangovideos.responses;


import org.json.JSONArray;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;

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