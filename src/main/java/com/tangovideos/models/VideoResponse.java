package com.tangovideos.models;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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


}
