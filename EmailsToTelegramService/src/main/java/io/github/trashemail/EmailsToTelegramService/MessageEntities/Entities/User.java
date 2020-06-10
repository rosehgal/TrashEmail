package io.github.trashemail.EmailsToTelegramService.MessageEntities.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
https://core.telegram.org/bots/api#user
 */
@Getter
@Setter
@NoArgsConstructor
public class User {
    private Long id;
    private Boolean is_bot;
    private String first_name;
    private String last_name;
    private String username;
    private String language_code;
    private Boolean can_join_groups;
    private Boolean can_read_all_group_messages;
    private Boolean supports_inline_queries;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", is_bot=" + is_bot +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", username='" + username + '\'' +
                ", language_code='" + language_code + '\'' +
                ", can_join_groups=" + can_join_groups +
                ", can_read_all_group_messages=" + can_read_all_group_messages +
                ", supports_inline_queries=" + supports_inline_queries +
                '}';
    }
}
