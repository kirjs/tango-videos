package com.tangovideos.resources;

import com.tangovideos.services.Interfaces.SongService;
import com.tangovideos.services.TangoVideosServiceFactory;
import org.json.JSONArray;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("api/songs")
@Produces(MediaType.APPLICATION_JSON)
public class SongResource {

    final SongService songService = TangoVideosServiceFactory.getSongService();

    @GET
    @Path("listNames")
    public Response list() {
        final String result = new JSONArray(songService.listNames()).toString();
        return Response.status(200)
                .entity(result)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
