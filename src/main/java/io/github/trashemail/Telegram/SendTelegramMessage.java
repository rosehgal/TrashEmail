package io.github.trashemail.Telegram;

import io.github.trashemail.Configurations.TelegramConfig;
import io.github.trashemail.Configurations.TrashemailConfig;
import io.github.trashemail.Telegram.DTO.TelegramResponse;
import io.github.trashemail.Telegram.DTO.messageEntities.InlineKeyboardButton;
import io.github.trashemail.Telegram.DTO.messageEntities.InlineKeyboardMarkup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
@EnableAsync
public class SendTelegramMessage {
    @Autowired
    private TelegramConfig telegramConfig;
    @Autowired
    private TrashemailConfig trashemailConfig;
    
    @Autowired
    RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(
            SendTelegramMessage.class);

    public ArrayList<String> chunks(String message){
        /*
        Checking the message size and do splitting into chunks if required
        */
        int maxMessageSize = telegramConfig.getSize();
        ArrayList<String> split = new ArrayList<>();
        for (int i = 0; i <= message.length() / maxMessageSize; i++) {
            split.add(message.substring(i * maxMessageSize,
                                        Math.min((i + 1) * maxMessageSize,
                                                 message.length())));
        }
        return split;
    }

    @Async
    public void sendMessage(String message, long chatId){
        this.sendMessage(
                message,
                chatId,
                null
        );
    }

    @Async
    public void sendMessage(String message, long chatId, String filename) {
        String telegramURI = telegramConfig.getUrl() +
                telegramConfig.getBotToken() +
                "/sendMessage";

        ArrayList<String> messageChunks = chunks(message);
        for (int i = 0; i < messageChunks.size(); i++) {
            TelegramResponse request = new TelegramResponse(
                    chatId,
                    messageChunks.get(i));

            ResponseEntity response = restTemplate.postForEntity(
                    telegramURI,
                    request,
                    TelegramResponse.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                log.debug("Message sent to user: " + chatId);
            }
            else
                log.error("Unable to send message to user: " + chatId);
        }

        if (filename != null) {
            /*
            Send HTML button back to user if everything is good with filename.
            */
            InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();

            InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
            keyboardButton.setText("HTML version");
            keyboardButton.setUrl(trashemailConfig.getHostURI() + filename);

            List<List<InlineKeyboardButton>> buttonList = new ArrayList<>();

            buttonList.add(new ArrayList<>());
            buttonList.get(0).add(keyboardButton);

            markupKeyboard.setInlineKeyboardButtonList(buttonList);

            TelegramResponse telegramResponse =
                new TelegramResponse(
                    chatId,
                    "To view in HTML format click the link below.",
                    markupKeyboard);

            ResponseEntity response = restTemplate.postForEntity(
                    telegramURI,
                    telegramResponse,
                    TelegramResponse.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                log.debug("HTML link sent to user: " + chatId + filename);
            }
            else log.error("Unable to HTML Link to user: " + chatId + filename);
        }
    }
}
