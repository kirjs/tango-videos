package com.tangovideos.models;

import com.google.api.client.util.Lists;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Set;

@XmlRootElement
public class Dancer {
    @XmlElement
    String name;
    private List<VideoResponse> videos = Lists.newArrayList();

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<VideoResponse> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoResponse> videos) {
        this.videos = videos;
    }
}
