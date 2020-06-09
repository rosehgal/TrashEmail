package io.github.trashemail.imapclientservice.Configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties("trashemail")
@Getter
@Setter
@NoArgsConstructor
public class TrashemailConfig {
    private String host;
    private String port;
    private String path;
}
