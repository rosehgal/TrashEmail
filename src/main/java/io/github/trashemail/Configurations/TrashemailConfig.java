package io.github.trashemail.Configurations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "trashemail")
public class TrashemailConfig {
    private Integer maxEmailsPerUser;

    @Override public String toString() {
        return "TrashemailConfig{" +
                "maxEmailsPerUser=" + maxEmailsPerUser +
                '}';
    }
}
