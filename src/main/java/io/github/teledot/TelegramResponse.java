package io.github.teledot;

/*
{
    "method": "sendMessage",
    "chat_id": 10000,
    "text": "text"
}
 */

public class TelegramResponse {
    private String method;
    private Integer chat_id;
    private String text;

    public TelegramResponse(String method, Integer chat_id, String text) {
        this.method = method;
        this.chat_id = chat_id;
        this.text = text;
    }

    public TelegramResponse(Integer chat_id, String text) {
        this.method = "sendMessage";
        this.chat_id = chat_id;
        this.text = text;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getChat_id() {
        return chat_id;
    }

    public void setChat_id(Integer chat_id) {
        this.chat_id = chat_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
