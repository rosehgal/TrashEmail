package io.github.trashemail.EmailsToTelegramService;

import io.github.trashemail.EmailsToTelegramService.Configuration.TrashemailConfig;
import io.github.trashemail.EmailsToTelegramService.utils.MailParser;
import io.github.trashemail.EmailsToTelegramService.utils.ParsedMail;
import io.github.trashemail.EmailsToTelegramService.utils.SaveMailToHTMLFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.Message;
import java.util.Map;

@Component
public class ForwardMailsToTelegram {

    @Autowired
    private TrashemailConfig trashemailConfig;
    @Autowired
    private SaveMailToHTMLFile saveMailToHTMLFile;
    @Autowired
    private SendTelegramMessage sendTelegramMessage;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MailParser mailParser;

    private static final Logger log = LoggerFactory.getLogger(
            ForwardMailsToTelegram.class);

    private long getChatIdFromEmailId(String emailFor) throws Exception {
        String url = "http://" +
                     trashemailConfig.getHost()+ ":" +
                     trashemailConfig.getPort() +
                     trashemailConfig.getPath() ;

        log.debug(url);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(url).queryParam("emailId", emailFor);

        log.debug(builder.toUriString());

        /*
        This is how a response might look like.
        {
          "id": 2, INT
          "chatId": 1234567890, LONG
          "emailId": "demo@trashemail.in", STRING
          "forwardsTo": "target@trashemail.in" STRING
        }
         */
        Map<String, Object> response = restTemplate.getForObject(
            builder.toUriString(),
            Map.class
        );
        Long chatId = (Long) ((Integer)response.get("chatId")).longValue();

        if(chatId == null){
            throw new Exception("Chat id not found ..");
        }
        return chatId;
    }

    public void sendToTelegram(Message message) throws Exception {
        ParsedMail parsedMail = mailParser.parseMail(message);

        for (String emailFor : parsedMail.getRecipients()) {
            /*
            Fetch chat if from TrashEmail service
            */
            Long chatId = null;
            try {
                chatId = getChatIdFromEmailId(emailFor);
            } catch (Exception e) {
                log.error("Unable to find chatId for EmailId: " + emailFor);
                e.printStackTrace();
                return;
            }

            /*
            If html content is set, offer to save in file and show html link.
            */
            if (parsedMail.getHtmlContentSet()) {
                Object filename = saveMailToHTMLFile.saveToFile(
                        parsedMail.getHtmlContent());

                if (filename != null)
                  sendTelegramMessage.sendMessage(parsedMail.toString(),
                                                  chatId,
                                                  (String) filename);
                else {
                  sendTelegramMessage.sendMessage(parsedMail.toString(),
                                                  chatId);
                }
            } else {
                sendTelegramMessage.sendMessage(parsedMail.toString(),
                                                chatId);
            }
        }
    }
}
