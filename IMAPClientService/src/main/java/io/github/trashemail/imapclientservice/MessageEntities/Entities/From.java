package io.github.trashemail.imapclientservice.MessageEntities.Entities;

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

	@Override
	public String toString() {
		return "From{" +
				"id=" + id +
				", is_bot=" + is_bot +
				", first_name='" + first_name + '\'' +
				", last_name='" + last_name + '\'' +
				", username='" + username + '\'' +
				", language_code='" + language_code + '\'' +
				'}';
	}
}