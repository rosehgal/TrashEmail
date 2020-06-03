package io.github.trashemail.Telegram.DTO.messageEntities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
https://core.telegram.org/bots/api#inlinekeyboardbutton
*/
@Getter
@Setter
@NoArgsConstructor
public class InlineKeyboardButton {
    private String text;
    private String callback_data;

    @Override
    public String toString() {
        return "InlineKeyboardButton{" +
                "text='" + text + '\'' +
                ", callback_data='" + callback_data + '\'' +
                '}';
    }
}
