package io.github.trashemail.EmailsToTelegramService;

import io.github.trashemail.EmailsToTelegramService.Configuration.ImapClientServiceConfig;
import io.github.trashemail.EmailsToTelegramService.MessageEntities.Entities.InlineKeyboardButton;
import io.github.trashemail.EmailsToTelegramService.MessageEntities.Entities.InlineKeyboardMarkup;
import io.github.trashemail.EmailsToTelegramService.MessageEntities.TelegramMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestTemplate;

import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
@EnableAsync
public class SendTelegramMessage {
    @Autowired
    private ImapClientServiceConfig imapClientServiceConfig;

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(
            SendTelegramMessage.class);

    public ArrayList<String> chunks(String message) {
        /*
        Checking the message size and do splitting into chunks if required
        */
        int maxMessageSize = imapClientServiceConfig.getTelegram().getSize();

        ArrayList<String> split = new ArrayList<>();
        for (int i = 0; i <= message.length() / maxMessageSize; i++) {
            split.add(message.substring(i * maxMessageSize,
                    Math.min((i + 1) * maxMessageSize,
                            message.length())));
        }
        return split;
    }

    @Async
    public void sendMessage(String message, long chatId) {
        this.sendMessage(
                message,
                chatId,
                null
        );
    }

    @Async
    public void sendMessage(String message, long chatId, String[] filename) {
        String telegramURI = imapClientServiceConfig.getTelegram().getUrl() +
                imapClientServiceConfig.getTelegram().getBotToken() +
                "/sendMessage";

        String escapedMessage = StringEscapeUtils.escapeHtml4(message);
        ArrayList<String> messageChunks = chunks(escapedMessage);

        for (int i = 0; i < messageChunks.size(); i++) {
            TelegramMessage request = new TelegramMessage(
                    chatId,
                    messageChunks.get(i),
                    "HTML");


            ResponseEntity response = restTemplate.postForEntity(
                    telegramURI,
                    request,
                    TelegramMessage.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                log.debug("Message sent to user: " + chatId);
            } else
                log.error("Unable to send message to user: " + chatId);
        }

        if (filename != null) {
            /*
            Send HTML button back to user if everything is good with filename.
            */
            InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();

            InlineKeyboardButton keyboardButtonSafeHTML = new InlineKeyboardButton();
            keyboardButtonSafeHTML.setText("Safe HTML version");
            keyboardButtonSafeHTML.setUrl(
                    imapClientServiceConfig.getEmails().getHostPath() +
                            filename[0]);

            InlineKeyboardButton keyboardButtonUnSafeHTML = new InlineKeyboardButton();
            keyboardButtonUnSafeHTML.setText("Unsafe HTML version");
            keyboardButtonUnSafeHTML.setUrl(
                    imapClientServiceConfig.getEmails().getHostPath() +
                            filename[1]);

            List<List<InlineKeyboardButton>> buttonList = new ArrayList<>();

            buttonList.add(new ArrayList<>());
            buttonList.get(0).add(keyboardButtonSafeHTML);
            buttonList.get(0).add(keyboardButtonUnSafeHTML);

            markupKeyboard.setInlineKeyboardButtonList(buttonList);

            TelegramMessage telegramResponse =
                    new TelegramMessage(
                            chatId,
                            "To view in HTML format click the link below.",
                            markupKeyboard);

            ResponseEntity response = restTemplate.postForEntity(
                    telegramURI,
                    telegramResponse,
                    TelegramMessage.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                log.debug("HTML link sent to user: " + chatId + filename);
            } else log.error("Unable to sent HTML Link to user: " + chatId + filename);
        }
    }

    @Async
    public void sendDocuments(String location, long chatId) {
        String telegramURI = imapClientServiceConfig.getTelegram().getUrl() +
                imapClientServiceConfig.getTelegram().getBotToken() +
                "/sendDocument";

        File file = new File(location);
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        try {
            bodyMap.add("document", getFileResource(location));
            bodyMap.add("chat_id", chatId);
            bodyMap.add("caption", file.getName());
        } catch (IOException e) {
            log.error("Unable to fetch the file: " + location);
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                telegramURI,
                HttpMethod.POST,
                requestEntity,
                String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            log.debug("File sent to user: " + chatId + location);
            file.delete();
        } else log.error("Unable to sent file to user: " + chatId + location);
    }

    public static Resource getFileResource(String location) throws IOException {
        return new FileSystemResource(location);
    }
}