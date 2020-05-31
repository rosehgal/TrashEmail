package io.github.trashemail.Telegram;

import io.github.trashemail.Configurations.TelegramConfg;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@EnableAsync
public class SendTelegramMessage {
    @Autowired
    private TelegramConfg telegramConfg;
    
    @Autowired
    RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(SendTelegramMessage.class);

    @Async
    public void sendMessage(String message, String chatId){
        String telegramURI = telegramConfg.getUrl() +
                             telegramConfg.getBotToken() +
                             "/sendMessage";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        // Checking the message size and do splitting into chunks if required
        int maxMessageSize = telegramConfg.getSize();
        ArrayList<String> split = new ArrayList<>();
        for (int i = 0; i <= message.length() / maxMessageSize; i++) {
        	split.add(message.substring(i * maxMessageSize, Math.min((i + 1) * maxMessageSize, message.length())));
        }

        for (int i = 0; i < split.size(); i++) {
        	MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        	data.add("chat_id", chatId);
        	data.add("text", split.get(i));

        	HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(data, headers);

        	ResponseEntity response = restTemplate.postForEntity(
        			telegramURI,
        			request,
        			String.class);

        	if(response.getStatusCode() == HttpStatus.OK){
        		log.debug("Message sent to user: " + chatId);
        	}
        	else
        		log.error("Unable to send message to user: " + chatId);
        }
    }
}
