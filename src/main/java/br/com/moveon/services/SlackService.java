package br.com.moveon.services;

import br.com.moveon.services.utils.Logger;
import br.com.moveon.services.dtos.SendNotificationRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SlackService extends AbstractService {

    private final ObjectMapper mapper = new ObjectMapper();

    private final HttpClient client = HttpClient.newHttpClient();
    private final String token = System.getenv("SLACK_BOT_TOKEN");


    public void sendMessage(String channel, String text) {
        try {
            SendNotificationRequestDTO sendNotificationRequestDTO = new SendNotificationRequestDTO(channel, text);
            String body = this.mapper.writeValueAsString(sendNotificationRequestDTO);

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create("https://slack.com/api/chat.postMessage"))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            client.send(req, HttpResponse.BodyHandlers.ofString()).body();

        } catch (Exception e) {
            logger.error("Ops hove um erro em enviar mensagem em canais publico no slack: " + e.getMessage());
            System.exit(0);
        }
    }
}
