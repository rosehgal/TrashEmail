package io.github.trashemail.Configurations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="telegram")
@Getter
@Setter
@NoArgsConstructor
public class TelegramConfig {
    /*
    TODO delete this class
     */
    private String botToken;
    private String url;
    private int size;
}
