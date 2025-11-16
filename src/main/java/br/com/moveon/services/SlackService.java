package br.com.moveon.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SlackService {

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String token = System.getenv("SLACK_BOT_TOKEN");

    // ---------------------------------------------------------
    // Enviar DM apenas chamando este método
    // ---------------------------------------------------------
    public void sendDirectMessage(String email, String message)
            throws IOException, InterruptedException {

        if (token == null)
            throw new RuntimeException("SLACK_BOT_TOKEN ausente");

        String userId = lookupUserId(email); // GET
        String channel = openDM(userId);     // POST
        postMessage(channel, message);       // POST
    }

    // ---------------------------------------------------------
    // 1) Buscar userId pelo e-mail
    // ---------------------------------------------------------
    private String lookupUserId(String email)
            throws IOException, InterruptedException {

        JsonNode json = getJson(
                "https://slack.com/api/users.lookupByEmail?email=" + email
        );

        if (!json.get("ok").asBoolean())
            throw new RuntimeException("lookupByEmail falhou: " + json);

        return json.get("user").get("id").asText();
    }

    // ---------------------------------------------------------
    // 2) Abrir canal de DM com o userId
    // ---------------------------------------------------------
    private String openDM(String userId)
            throws IOException, InterruptedException {

        String body = "{ \"users\": \"" + userId + "\" }";

        JsonNode json = postJson(
                "https://slack.com/api/conversations.open",
                body
        );

        if (!json.get("ok").asBoolean())
            throw new RuntimeException("conversations.open falhou: " + json);

        return json.get("channel").get("id").asText();
    }

    // ---------------------------------------------------------
    // 3) Enviar mensagem para o canal da DM
    // ---------------------------------------------------------
    private void postMessage(String channel, String text)
            throws IOException, InterruptedException {

        String body = "{ \"channel\": \"" + channel + "\", \"text\": \"" + text + "\" }";

        JsonNode json = postJson(
                "https://slack.com/api/chat.postMessage",
                body
        );

        if (!json.get("ok").asBoolean())
            throw new RuntimeException("chat.postMessage falhou: " + json);
    }

    // ---------------------------------------------------------
    // Método público opcional para enviar em canais públicos
    // ---------------------------------------------------------
    public void sendMessageToChannel(String channel, String text) {
        try {
            postMessage(channel, text);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ---------------------------------------------------------
    // Helpers genéricos (reduzem 30 linhas)
    // ---------------------------------------------------------
    private JsonNode getJson(String url)
            throws IOException, InterruptedException {

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        String body = client.send(req, HttpResponse.BodyHandlers.ofString()).body();
        return mapper.readTree(body);
    }

    private JsonNode postJson(String url, String body)
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
