package com.tangovideos.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserNotLoggedIn {

    @XmlElement
    Boolean authentificated = false;
}
