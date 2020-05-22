package io.github.teledot.SmtpServer;

import io.github.teledot.EmailInteraction.FetchEmailAndSendTelegramMessages;
import org.apache.commons.mail.util.MimeMessageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subethamail.smtp.helper.SimpleMessageListener;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.Properties;

public class SimpleMessageListenerImpl implements SimpleMessageListener {
    private static final Logger log = LoggerFactory.getLogger(FetchEmailAndSendTelegramMessages.class);

    public SimpleMessageListenerImpl() {
    }

    @Override
    public boolean accept(String from, String recipient) {
        return true;
    }

    @Override
    public void deliver(String from, String recipient, InputStream data) {
        Session session = Session.getDefaultInstance(new Properties());
        MimeMessage m = null;
        try {
            m = new MimeMessage(session, data);
            MimeMessageParser parser = new MimeMessageParser(m);
            String htmlContent = parser.getHtmlContent();
            log.info("Printing Data to console");
            log.info(parser.getFrom());

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}