package com.tangovideos.exceptions;

import org.json.JSONObject;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AnyExceptionMapper implements ExceptionMapper<Exception> {
    public Response toResponse(Exception ex) {
        final ErrorMessage entity = new ErrorMessage(ex);
        ex.printStackTrace();
        return Response.status(entity.getStatus()).
                entity(new JSONObject(entity).toString()).
                type(MediaType.APPLICATION_JSON).
                build();
    }
}
