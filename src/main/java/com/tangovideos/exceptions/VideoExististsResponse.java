package com.tangovideos.exceptions;


import org.apache.commons.beanutils.BeanUtils;

import javax.ws.rs.NotFoundException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.lang.reflect.InvocationTargetException;

@XmlRootElement
public class VideoExististsResponse {
    @XmlElement(name = "exists")
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
