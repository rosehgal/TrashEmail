package io.github.teledot.Telegram;

import io.github.teledot.Configurations.TelegramConfg;
import io.github.teledot.EmailInteraction.ImapClient;
import io.github.teledot.Respositories.UserRepository;
import io.github.teledot.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class SendTelegramMessage {
    @Autowired
    private TelegramConfg telegramConfg;

    private static final Logger log = LoggerFactory.getLogger(SendTelegramMessage.class);

    public void sendMessage(String message, String chatId){
        String telegramURI = telegramConfg.getUrl() +
                             telegramConfg.getBotToken() +
                             "/sendMessage";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("chat_id", chatId);
        data.add("text", message);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(data, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity response = restTemplate.postForEntity(
                telegramURI,
                request,
                String.class);

        if(response.getStatusCode() == HttpStatus.OK){
            log.info("Message sent to user: " + chatId);
        }

    }
}
