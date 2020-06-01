package io.github.trashemail.Telegram.DTO;

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
@Getter @Setter @NoArgsConstructor
public class TelegramRequest {
    private long update_id;
    private Message message;
}
