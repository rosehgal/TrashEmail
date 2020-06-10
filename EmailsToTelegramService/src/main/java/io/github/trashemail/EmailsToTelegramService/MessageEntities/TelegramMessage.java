package io.github.trashemail.EmailsToTelegramService.MessageEntities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.trashemail.EmailsToTelegramService.MessageEntities.Entities.InlineKeyboardMarkup;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
{
    "method": "sendMessage",
    "chat_id": 10000,
    "text": "text"
}

Detailed explanation of how Telegram response might look like as message :
https://core.telegram.org/bots/api#message
 */


@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TelegramMessage {
    private String method;
    private long chat_id;
    private String text;
    private String parse_mode;
    private InlineKeyboardMarkup inlineKeyboardMarkup;

    @JsonProperty("reply_markup")
    public InlineKeyboardMarkup getInlineKeyboardMarkup() {
        return inlineKeyboardMarkup;
    }

    public TelegramMessage(String method, long chat_id, String text,
                            String parse_mode,
                            InlineKeyboardMarkup inlineKeyboardMarkup) {
        this.method = method;
        this.chat_id = chat_id;
        this.text = text;
        this.parse_mode = parse_mode;
        this.inlineKeyboardMarkup = inlineKeyboardMarkup;
    }

    public TelegramMessage(long chat_id, String text) {
        this("sendMessage",
             chat_id,
             text,
             "Markdown",
             null);
    }

    public TelegramMessage(long chat_id, String text, String parse_mode){
        this("sendMessage",
             chat_id,
             text,
             parse_mode,
             null);
    }

    public TelegramMessage(String method, long chat_id, String text,
                            String parse_mode){
        this(method,
             chat_id,
             text,
             parse_mode,
             null);
    }

    public TelegramMessage(long chat_id, String text,
                            InlineKeyboardMarkup inlineKeyboardMarkup){
        this("sendMessage",
             chat_id,
             text,
             "Markdown",
             inlineKeyboardMarkup);
    }

    @Override
    public String toString() {
        return "TelegramResponse{" +
                "method='" + method + '\'' +
                ", chat_id=" + chat_id +
                ", text='" + text + '\'' +
                ", parse_mode='" + parse_mode + '\'' +
                ", inlineKeyboardMarkup=" + inlineKeyboardMarkup +
                '}';
    }
}
