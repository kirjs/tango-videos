package com.tangovideos.resources.inputs;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class JustName implements Serializable {
    @XmlElement
    String name;

    public static JustName fromName(String name) {
        final JustName justName = new JustName();
        justName.setName(name);
        return justName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
