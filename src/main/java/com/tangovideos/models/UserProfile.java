package com.tangovideos.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserProfile {

    @XmlElement
    Boolean authentificated = true;

    @XmlElement
    String id = "ad min";
}
