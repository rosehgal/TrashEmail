package io.github.trashemail.Configurations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "trashemail")
public class TrashEmailConfig {
    private String version;
    private List<String> connectorURLs;

    @Override public String toString() {
        return "TrashEmailConfig{" +
                "version='" + version + '\'' +
                '}';
    }
}
