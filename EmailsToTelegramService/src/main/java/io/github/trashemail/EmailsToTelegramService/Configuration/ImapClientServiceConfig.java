package io.github.trashemail.EmailsToTelegramService.Configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Getter
@Setter
@NoArgsConstructor
@Configuration
@ConfigurationProperties("imap-client-service")
public class ImapClientServiceConfig {
    private Imap imap;
    private Telegram telegram;
    private Emails emails;

    @Getter
    @Setter
    @NoArgsConstructor
    @Configuration
    @ConfigurationProperties("imap")
    public static class Imap{
        private String host;
        private String port;
        private String email;
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @Configuration
    @ConfigurationProperties("telegram")
    public static class Telegram{
        private String botToken;
        private String url;
        private int size;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @Configuration
    @ConfigurationProperties("emails")
    public static class Emails{
        private String hostPath;
        private String downloadPath;
    }
}
