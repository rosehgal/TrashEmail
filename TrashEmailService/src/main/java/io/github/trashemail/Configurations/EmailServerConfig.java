package io.github.trashemail.Configurations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "email-server")
@Getter @Setter @NoArgsConstructor
public class EmailServerConfig {

    private List<String> hosts;
    private String adminEmail;
    private String adminPassword;
    private String addUrl;
    private String removeUrl;
    private List<String> targetAlias;

    @Override public String toString() {
        return "EmailServerConfig{" +
                "hosts=" + hosts +
                ", adminEmail='" + adminEmail + '\'' +
                ", addUrl='" + addUrl + '\'' +
                ", removeUrl='" + removeUrl + '\'' +
                ", targetAlias='" + targetAlias + '\'' +
                '}';
    }
}
