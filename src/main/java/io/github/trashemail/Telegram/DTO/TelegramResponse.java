package io.github.trashemail.Telegram.DTO;

/*
{
    "method": "sendMessage",
    "chat_id": 10000,
    "text": "text"
}
 */

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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
}
