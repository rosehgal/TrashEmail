package io.github.teledot.Telegram;

import io.github.teledot.EmailInteraction.ImapClient;
import io.github.teledot.Respositories.UserRepository;
import io.github.teledot.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;

@Component
public class ForwardMailsToTelegram {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SendTelegramMessage sendTelegramMessage;

    private static final Logger log = LoggerFactory.getLogger(ForwardMailsToTelegram.class);

    public void sendToTelegram(Message message) throws MessagingException {
        String emailFor = message.getAllRecipients()[0].toString();
        log.debug("Got Message for "+ emailFor);
        User user = userRepository.findByEmailId(emailFor);
        String data = message.getSubject() + "\n" + message.getFrom()[0].toString();

        sendTelegramMessage.sendMessage(data, Integer.toString(user.getChatId()));
    }
}
