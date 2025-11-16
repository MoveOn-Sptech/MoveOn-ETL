package br.com.moveon.services.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SlackClientApiUtil {

    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient client = HttpClient.newHttpClient();
    private final String token = System.getenv("SLACK_BOT_TOKEN");

    public JsonNode getJson(String url)
            throws IOException, InterruptedException {

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        String body = client.send(req, HttpResponse.BodyHandlers.ofString()).body();
        return mapper.readTree(body);
    }

    public JsonNode postJson(String url, String body)
            throws IOException, InterruptedException {

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        String response = client.send(req, HttpResponse.BodyHandlers.ofString()).body();
        return mapper.readTree(response);
    }

}
