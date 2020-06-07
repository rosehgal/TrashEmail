package io.github.trashemail.Telegram;

import io.github.trashemail.Respositories.UserRepository;
import io.github.trashemail.models.User;
import io.github.trashemail.utils.MailParser;
import io.github.trashemail.utils.SaveMailToHTMLFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.Message;

@Component
public class ForwardMailsToTelegram {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SendTelegramMessage sendTelegramMessage;
    @Autowired
    private SaveMailToHTMLFile saveMailToHTMLFile;

    private static final Logger log = LoggerFactory.getLogger(
            ForwardMailsToTelegram.class);

    public void sendToTelegram(Message message) throws Exception {
        String emailFor = message.getAllRecipients()[0].toString();
        User user = userRepository.findByEmailId(emailFor);
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
                        user.getChatId(),
                        (String) filename
                );
            else {
                sendTelegramMessage.sendMessage(parsedMail.toString(),
                                                user.getChatId()
                );
            }
        }
        else {
          sendTelegramMessage.sendMessage(parsedMail.toString(),
                                          user.getChatId());
            }
    }
}
