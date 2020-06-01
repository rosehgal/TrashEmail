package io.github.trashemail.Telegram.DTO.messageEntities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class Message {
	private long message_id;

	private From from;
	private Chat chat;

	private Date date;
	private String text;

}