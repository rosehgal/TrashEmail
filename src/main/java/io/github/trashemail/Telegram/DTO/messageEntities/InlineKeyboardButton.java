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
    private String url;
}
