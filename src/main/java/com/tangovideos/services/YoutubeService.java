package com.tangovideos.services;

import com.google.api.client.util.Lists;
import com.tangovideos.models.Channel;
import com.tangovideos.models.Video;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class YoutubeService {
    final String BASE_URL = "https://www.googleapis.com/youtube/v3/";
    final String API_TOKEN = "AIzaSyCW22iBpph-9DMs3rpHa3iXZDpTV0qsLCU";

    public List<Video> fetchChannelVideos(String id, long startingFrom) {
        return fetchChannelVideos(id, startingFrom, "");
    }

    public List<Video> fetchChannelVideos(String id, long startingFrom, String pageToken) {
        final ArrayList<Video> result = Lists.newArrayList();
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_URL).path("playlistItems")
                .queryParam("key", API_TOKEN)
                .queryParam("playlistId", id)
                .queryParam("maxResults", 50)
                .queryParam("pageToken", pageToken)
                .queryParam("part", "snippet");

        String response = target.request("application/json").get(new GenericType<>(String.class));
        final JSONObject jsonResponse = new JSONObject(response);
        final JSONArray items = jsonResponse.getJSONArray("items");
        for (int i = 0; i < items.length(); i++) {
            final JSONObject snippet = items.getJSONObject(i).getJSONObject("snippet");
            final long publishedAt = Instant.parse(snippet.getString("publishedAt")).getEpochSecond();
            if (publishedAt <= startingFrom) {
                return result;
            }

            final String videoId = snippet.getJSONObject("resourceId").getString("videoId");
            result.add(snippetToVideo(videoId, snippet));
        }
        final String nextPageToken = jsonResponse.optString("nextPageToken");
        if (!nextPageToken.isEmpty()) {
            result.addAll(fetchChannelVideos(id, startingFrom, nextPageToken));
        }

        return result;
    }

    public Video getVideoInfo(String id) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_URL).path("videos")
                .queryParam("key", API_TOKEN)
                .queryParam("id", id)
                .queryParam("part", "contentDetails");

        String response = target.request("application/json").get(new GenericType<>(String.class));

        try {
            JSONObject snippet = new JSONObject(response)
                    .getJSONArray("items")
                    .getJSONObject(0)
                    .getJSONObject("snippet");

            return snippetToVideo(id, snippet);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Video snippetToVideo(String id, JSONObject snippet) {
        final Video result = new Video(
                id,
                snippet.getString("title"),
                snippet.getString("publishedAt")
        );
        result.setDescription(snippet.getString("description"));
        result.setDancers(BasicNameParser.extractNames(result.getTitle(), result.getDescription()));
        return result;
    }

    public Channel getChannelInfoById(String id) {
        return extractChannelInfo(fetchData(id, "id", "channels", "contentDetails,snippet"), id);
    }

    private JSONArray fetchData(String id, String filter, String path, String part) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_URL).path(path)
                .queryParam("key", API_TOKEN)
                .queryParam(filter, id)
                .queryParam("part", part);

        String response = target.request("application/json").get(new GenericType<>(String.class));
        try {
            return new JSONObject(response).getJSONArray("items");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private Channel extractChannelInfo(JSONArray items, String id) {
        String uploadPlaylistId = items.getJSONObject(0)
                .getJSONObject("contentDetails")
                .getJSONObject("relatedPlaylists")
                .getString("uploads");

        String title = items.getJSONObject(0).getJSONObject("snippet").getString("title");
        Channel channel = new Channel(id);
        channel.setUploadPlaylistId(uploadPlaylistId);
        channel.setTitle(title);
        return channel;
    }
}
