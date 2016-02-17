package com.tangovideos.resources;

import com.tangovideos.resources.inputs.RenameDancer;
import com.tangovideos.services.Interfaces.AdminToolsService;
import com.tangovideos.services.TangoVideosServiceFactory;
import org.json.JSONArray;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("api/adminTools")
@Produces(MediaType.APPLICATION_JSON)
public class AdminToolsResource {


    private AdminToolsService adminToolsService = TangoVideosServiceFactory.getAdminToolsService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("renameDancer")
    public Response renameDancer(RenameDancer payload) {
        adminToolsService.renameDancer(payload.getOldName(), payload.getNewName());
        return Response.status(200)
                .entity("true")
                .type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("stats")
    public Response stats() {
        return Response.status(200)
                .entity(new JSONArray(adminToolsService.stats()).toString())
                .type(MediaType.APPLICATION_JSON).build();
    }


}
