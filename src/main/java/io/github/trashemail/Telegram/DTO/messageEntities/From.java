package io.github.trashemail.Telegram.DTO.messageEntities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class From {
	private long id;
	private boolean is_bot;
	private String first_name;
	private String last_name;
	private String username;
	private String language_code;
}