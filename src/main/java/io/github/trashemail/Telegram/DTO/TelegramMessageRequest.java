package io.github.trashemail.Telegram.DTO;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.trashemail.Telegram.DTO.messageEntities.CallbackQuery;
import io.github.trashemail.Telegram.DTO.messageEntities.Message;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/* This is how a response looks like, which we are taking input as request
{
	"update_id": 90,
	"message": {
		"message_id": 6,
		"from": {
			"id": 9088787XXX,
			"is_bot": false,
			"first_name": "Rohit",
			"last_name": "Sehgal",
			"username": "r0hi7",
			"language_code": "en"
		},
		"chat": {
			"id": 753469447,
			"first_name": "Rohit",
			"last_name": "Sehgal",
			"username": "r0hi7",
			"type": "private"
		},
		"date": 1588833291,
		"text": "Hi"
	}
}

 */
@Getter
@Setter
@NoArgsConstructor
public class TelegramMessageRequest {
    private long update_id;
    private JsonNode message;
    private JsonNode callbck_query;

    @Override
    public String toString() {
        return "TelegramMessageRequest{" +
                "update_id=" + update_id +
                ", message=" + message +
                ", callbck_query=" + callbck_query +
                '}';
    }
}
