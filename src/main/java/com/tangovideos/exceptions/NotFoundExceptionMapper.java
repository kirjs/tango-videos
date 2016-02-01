package com.tangovideos.exceptions;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
    public Response toResponse(NotFoundException ex) {
        return Response.status(404).
                entity(new ErrorMessage(ex)).
                type(MediaType.APPLICATION_JSON).
                build();
    }

    public Response toResponse(VideoExistsException ex) {
        return Response.status(200).
                entity(new ErrorMessage(ex)).
                type(MediaType.APPLICATION_JSON).
                build();
    }
}
