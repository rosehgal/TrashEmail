package io.github.trashemail.EmailsToTelegramService.MessageEntities.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
https://core.telegram.org/bots/api#callbackquery
 */
@Getter
@Setter
@NoArgsConstructor
public class CallbackQuery {
    private String id;
    private User from;
    private Message message;
    private String inline_message_id;
    private String chat_instance;
    private String data;
    private String game_short_name;

    @Override
    public String toString() {
        return "CallbackQuery{" +
                "id='" + id + '\'' +
                ", from=" + from +
                ", message=" + message +
                ", inline_message_id='" + inline_message_id + '\'' +
                ", chat_instance='" + chat_instance + '\'' +
                ", data='" + data + '\'' +
                ", game_short_name='" + game_short_name + '\'' +
                '}';
    }
}
