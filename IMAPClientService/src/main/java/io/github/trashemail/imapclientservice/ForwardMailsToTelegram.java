package io.github.trashemail.imapclientservice;

import io.github.trashemail.imapclientservice.Configuration.ImapClientServiceConfig;
import io.github.trashemail.imapclientservice.Configuration.TrashemailConfig;
import io.github.trashemail.imapclientservice.utils.MailParser;
import io.github.trashemail.imapclientservice.utils.SaveMailToHTMLFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
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
          "chatId": 753469447, LONG
          "emailId": "rohitji@trashemail.in", STRING
          "forwardsTo": "testtrashmemailbot@trashemail.in" STRING
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
        String emailFor = message.getAllRecipients()[0].toString();

        /*
        Fetch chat if from TrashEmail service
        */

        Long chatId = null;
        try{
            chatId = getChatIdFromEmailId(emailFor);
        } catch(Exception e){
            log.error("Unable to find chatId for EmailId: " + emailFor);
            e.printStackTrace();
            return;
        }

        MailParser parsedMail = new MailParser(message);

        /*
        If html content is set, offer to save in file and show html link.
        */
        if(parsedMail.getHtmlContentSet()){
            Object filename = saveMailToHTMLFile.saveToFile(
                    parsedMail.getHtmlContent()
            );

            if (filename != null)
                sendTelegramMessage.sendMessage(
                        parsedMail.toString(),
                        chatId,
                        (String) filename
                );
            else {
                sendTelegramMessage.sendMessage(parsedMail.toString(),
                                                chatId
                );
            }
        }
        else {
            sendTelegramMessage.sendMessage(parsedMail.toString(),
                                            chatId);
        }
    }
}
