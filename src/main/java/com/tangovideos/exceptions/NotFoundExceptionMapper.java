package com.tangovideos.exceptions;

import com.tangovideos.models.ErrorMessage;

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
}
