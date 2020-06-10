package io.github.trashemail.Telegram.DTO.messageEntities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
https://core.telegram.org/bots/api#inlinekeyboardbutton
*/
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InlineKeyboardButton {
    /*
    According the docs, either of callback_data or uri, only one should be set.
    */
    private String text;
    private String callback_data;
    private String url;

    @Override public String toString() {
        return "InlineKeyboardButton{" +
                "text='" + text + '\'' +
                ", callback_data='" + callback_data + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
