package com.tangovideos.resources;

import com.tangovideos.resources.inputs.RenameDancer;
import com.tangovideos.services.Interfaces.AdminToolsService;
import com.tangovideos.services.TangoVideosServiceFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("api/adminTools")
@Produces(MediaType.APPLICATION_JSON)
public class AdminToolsResource {


    private AdminToolsService adminToolsService = TangoVideosServiceFactory.getAdminToolsService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("listNames")
    public Response listNames(RenameDancer payload) {


        adminToolsService.renameDancer(payload.getOldName(), payload.getNewName());
        return Response.status(200)
                .entity("true")
                .type(MediaType.APPLICATION_JSON).build();
    }


}
