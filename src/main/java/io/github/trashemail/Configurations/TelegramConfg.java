package io.github.trashemail.Configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="telegram")
public class TelegramConfg{
    private String botToken;
    private String url;
    private int size=4096;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public int getSize() {
    	return size;
    }

    public void setSize(int size) {
    	this.size = size;
    }
}
