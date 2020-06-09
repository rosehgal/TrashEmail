package io.github.trashemail.imapclientservice.MessageEntities.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
https://core.telegram.org/bots/api#messageentity
 */
@Getter
@Setter
@NoArgsConstructor
public class MessageEntity {
    private String type;
    private Integer offset;
    private Integer length;
    private String url;
    private User user;
    private String language;

    @Override
    public String toString() {
        return "MessageEntity{" +
                "type='" + type + '\'' +
                ", offset=" + offset +
                ", length=" + length +
                ", url='" + url + '\'' +
                ", user=" + user +
                ", language='" + language + '\'' +
                '}';
    }
}
