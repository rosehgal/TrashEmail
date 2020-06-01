package io.github.trashemail.Telegram.DTO.messageEntities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Chat {
	private long id;
	private String first_name;
	private String last_name;
	private String username;
	private String type;
}