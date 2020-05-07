package io.github.teledot;

import java.util.Date;

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
public class TelegramRequest {
    private Integer update_id;
    private Message message;

    public TelegramRequest() {
    }

    public class Message{
        private Integer message_id;
        private From from;
        private Chat chat;
        private Date date;
        private String text;

        public Message() {
        }

        public class Chat{
            private Integer id;
            private String first_name;
            private String last_name;
            private String username;
            private String type;

            public Chat() {
            }

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getFirst_name() {
                return first_name;
            }

            public void setFirst_name(String first_name) {
                this.first_name = first_name;
            }

            public String getLast_name() {
                return last_name;
            }

            public void setLast_name(String last_name) {
                this.last_name = last_name;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

        private class From{
            private Integer id;
            private boolean is_bot;
            private String first_name;
            private String last_name;
            private String username;
            private String language_code;

            public From() {
            }

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public boolean isIs_bot() {
                return is_bot;
            }

            public void setIs_bot(boolean is_bot) {
                this.is_bot = is_bot;
            }

            public String getFirst_name() {
                return first_name;
            }

            public void setFirst_name(String first_name) {
                this.first_name = first_name;
            }

            public String getLast_name() {
                return last_name;
            }

            public void setLast_name(String last_name) {
                this.last_name = last_name;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getLanguage_code() {
                return language_code;
            }

            public void setLanguage_code(String language_code) {
                this.language_code = language_code;
            }
        }

        public Chat getChat() {
            return chat;
        }

        public void setChat(Chat chat) {
            this.chat = chat;
        }

        public Integer getMessage_id() {
            return message_id;
        }

        public void setMessage_id(Integer message_id) {
            this.message_id = message_id;
        }

        public From getFrom() {
            return from;
        }

        public void setFrom(From from) {
            this.from = from;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public Integer getUpdate_id() {
        return update_id;
    }

    public void setUpdate_id(Integer update_id) {
        this.update_id = update_id;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
