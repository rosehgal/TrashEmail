package io.github.trashemail.Telegram;

import io.github.trashemail.Respositories.UserRepository;
import io.github.trashemail.models.User;
import io.github.trashemail.utils.MailParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;

@Component
public class ForwardMailsToTelegram {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SendTelegramMessage sendTelegramMessage;

    private static final Logger log = LoggerFactory.getLogger(ForwardMailsToTelegram.class);

    public void sendToTelegram(Message message) throws MessagingException, IOException {
        String emailFor = message.getAllRecipients()[0].toString();
        User user = userRepository.findByEmailId(emailFor);
        MailParser parsedMail = new MailParser(message);

        sendTelegramMessage.sendMessage(parsedMail.toString(), Integer.toString(user.getChatId()));
    }
}
