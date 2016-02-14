package com.tangovideos.models;


import com.google.api.client.util.Lists;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Set;

@XmlRootElement
public class VideoResponse {
    @XmlElement
    public Set<String> dancers;

    @XmlElement
    public String id;

    @XmlElement
    public String title;

    @XmlElement
    public String publishedAt;

    @XmlElement
    private String addedAt;

    @XmlElement
    private List<Song> songs = Lists.newArrayList();
    private String recordedAt;

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Set<String> getDancers() {
        return dancers;
    }

    public void setDancers(Set<String> dancers) {
        this.dancers = dancers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void setAddedAt(String addedAt) {
        this.addedAt = addedAt;
    }

    public String getAddedAt() {
        return addedAt;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public void setRecordedAt(String recordedAt) {
        this.recordedAt = recordedAt;
    }

    public String getRecordedAt() {
        return recordedAt;
    }
}
