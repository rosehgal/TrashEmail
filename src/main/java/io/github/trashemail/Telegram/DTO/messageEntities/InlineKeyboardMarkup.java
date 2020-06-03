package io.github.trashemail.Telegram.DTO.messageEntities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/*
    https://core.telegram.org/bots/api#inlinekeyboardmarkup
*/
@Getter
@Setter
@NoArgsConstructor
public class InlineKeyboardMarkup {
    List<List<InlineKeyboardButton>> inlineKeyboardButtonList;

    @JsonProperty("inline_keyboard")
    public List<List<InlineKeyboardButton>> getInlineKeyboardButtonList() {
        return inlineKeyboardButtonList;
    }

    @Override
    public String toString() {
        return "InlineKeyboardMarkup{" +
                "inlineKeyboardButtonList=" + inlineKeyboardButtonList +
                '}';
    }
}
