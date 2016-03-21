package com.tangovideos.resources.inputs;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class EventUpdate implements Serializable {
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventInstance() {
        return eventInstance;
    }

    public void setEventInstance(String eventInstance) {
        this.eventInstance = eventInstance;
    }

    @XmlElement
    String eventName;
    @XmlElement
    String eventInstance;

}
