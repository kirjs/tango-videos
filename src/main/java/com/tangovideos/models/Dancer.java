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

    @XmlElement
    private List<Video> videos = Lists.newArrayList();
    private Set<String> pseudonyms;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public Set<String>  getPseudonyms() {
        return pseudonyms;
    }

    public void setPseudonyms(Set<String>  pseudonyms) {
        this.pseudonyms = pseudonyms;
    }
}
