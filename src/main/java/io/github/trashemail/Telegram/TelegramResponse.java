package io.github.trashemail.Telegram;

/*
{
    "method": "sendMessage",
    "chat_id": 10000,
    "text": "text"
}
 */

public class TelegramResponse {
    private String method;
    private long chat_id;
    private String text;
    private String parse_mode;

    public TelegramResponse(String method, long chat_id, String text) {
        this.method = method;
        this.chat_id = chat_id;
        this.text = text;
        this.parse_mode = "Markdown";
    }

    public TelegramResponse(long chat_id, String text) {
        this.method = "sendMessage";
        this.chat_id = chat_id;
        this.text = text;
        this.parse_mode = "Markdown";
    }

    public String getParse_mode() {
        return parse_mode;
    }

    public void setParse_mode(String parse_mode) {
        this.parse_mode = parse_mode;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public long getChat_id() {
        return chat_id;
    }

    public void setChat_id(long chat_id) {
        this.chat_id = chat_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
