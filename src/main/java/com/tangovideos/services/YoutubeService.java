package com.tangovideos.services;

import com.tangovideos.models.Video;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

public class YoutubeService {
    final String BASE_URL = "https://www.googleapis.com/youtube/v3/";
    final String API_TOKEN = "AIzaSyCW22iBpph-9DMs3rpHa3iXZDpTV0qsLCU";

    public Video getVideoInfo(String id) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_URL).path("videos")
                .queryParam("key", API_TOKEN)
                .queryParam("id", id)
                .queryParam("part", "snippet");
        String response = target.request("application/json").get(new GenericType<>(String.class));
        try {
            JSONObject snippet = new JSONObject(response)
                    .getJSONArray("items")
                    .getJSONObject(0)
                    .getJSONObject("snippet");

            final Video result = new Video(
                    id,
                    snippet.getString("title"),
                    snippet.getString("publishedAt")
            );
            result.setDescription(snippet.getString("description"));
            result.setDancers(BasicNameParser.extractNames(result.getTitle(), result.getDescription()));
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
