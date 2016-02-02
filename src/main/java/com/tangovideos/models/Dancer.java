package com.tangovideos.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Dancer {
    @XmlElement
    String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
