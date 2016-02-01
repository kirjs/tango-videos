package com.tangovideos.responses;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;

@XmlRootElement
public class VideosResponse {
    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }

    @XmlElement(name = "videos")
    List<Map<String, Object>> list;

}