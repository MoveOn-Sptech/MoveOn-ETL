package br.com.moveon.services;

import br.com.moveon.providers.Logger;
import br.com.moveon.services.dtos.OpenDmByUserIdRequestDTO;
import br.com.moveon.services.dtos.SendNotificationRequestDTO;
import br.com.moveon.services.utils.SlackClientApiUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class SlackService {

    private final ObjectMapper mapper = new ObjectMapper();
    private final String token = System.getenv("SLACK_BOT_TOKEN");
    private final SlackClientApiUtil slackClientApiUtil = new SlackClientApiUtil();

    private Logger logger;

    public SlackService(Logger logger) {
        this.logger = logger;
    }

    public void sendDirectMessage(String email, String message) {

        try {
            if (token == null)
                throw new RuntimeException("SLACK_BOT_TOKEN ausente");

            String userId = lookupUserId(email); // GET
            String channel = openDM(userId);     // POST
            postMessage(channel, message);       // POST
        } catch (Exception e) {
            logger.error("Ops hove um erro em enviar mensagens no direct: " + e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }
    }


    private String lookupUserId(String email)
            throws IOException, InterruptedException {

        JsonNode json = this.slackClientApiUtil.getJson(
                "https://slack.com/api/users.lookupByEmail?email=" + email
        );

        if (!json.get("ok").asBoolean())
            throw new RuntimeException("lookupByEmail falhou: " + json);

        return json.get("user").get("id").asText();
    }

    private String openDM(String userId)
            throws IOException, InterruptedException {
        OpenDmByUserIdRequestDTO openDmByUserIdRequestDTO = new OpenDmByUserIdRequestDTO(userId);
        String body = this.mapper.writeValueAsString(openDmByUserIdRequestDTO);

        JsonNode json = this.slackClientApiUtil.postJson(
                "https://slack.com/api/conversations.open",
                body
        );

        if (!json.get("ok").asBoolean())
            throw new RuntimeException("conversations.open falhou: " + json);

        return json.get("channel").get("id").asText();
    }

    private void postMessage(String channel, String text)
            throws IOException, InterruptedException {
        SendNotificationRequestDTO sendNotificationRequestDTO = new SendNotificationRequestDTO(channel, text);
        String body = this.mapper.writeValueAsString(sendNotificationRequestDTO);

        JsonNode json = this.slackClientApiUtil.postJson(
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
            logger.error("Ops hove um erro em enviar mensagem em canais publico no slack: " + e.getMessage());
            System.exit(0);
        }
    }
}
