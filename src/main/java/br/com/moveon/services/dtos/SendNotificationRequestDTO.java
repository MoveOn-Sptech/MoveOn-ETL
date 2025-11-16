package br.com.moveon.services.dtos;

public class SendNotificationRequestDTO {

    private String channel;
    private String text;

    public SendNotificationRequestDTO() {}

    public SendNotificationRequestDTO(String channel, String text) {
        this.channel = channel;
        this.text = text;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
