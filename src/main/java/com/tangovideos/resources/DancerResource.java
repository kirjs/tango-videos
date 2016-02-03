package com.tangovideos.resources;

import com.tangovideos.services.TangoVideosServiceFactory;
import org.json.JSONArray;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("api/dancers")
@Produces(MediaType.APPLICATION_JSON)
public class DancerResource {
    @GET
    @Path("list")
    public Response list() {
        return Response.status(200)
                .entity(new JSONArray(TangoVideosServiceFactory.getDancerService().list()).toString())
                .type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("{id}")
    public Response getVideo(@PathParam("id") String id) {
        return Response.status(200)
                .entity(TangoVideosServiceFactory.getDancerService().get(id))
                .type(MediaType.APPLICATION_JSON).build();
    }
}
