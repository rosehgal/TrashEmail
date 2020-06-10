package io.github.trashemail.EmailsToTelegramService.MessageEntities.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Message {
	private long message_id;

	private User from;
	private Chat chat;

	private Date date;
	private String text;

	private List<MessageEntity> entities;
	private InlineKeyboardMarkup reply_markup;

	@Override
	public String toString() {
		return "Message{" +
				"message_id=" + message_id +
				", from=" + from +
				", chat=" + chat +
				", date=" + date +
				", text='" + text + '\'' +
				", entities=" + entities +
				", reply_markup=" + reply_markup +
				'}';
	}
}