package io.github.trashemail.utils;

import io.github.trashemail.Telegram.ForwardMailsToTelegram;
import org.apache.commons.mail.util.MimeMessageParser;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import java.util.Date;

public class MailParser {
    private String to;
    private String from;
    private String subject;
    private String content;
    private Date date;

    public MailParser(Message message) throws Exception {
        this.content="";
        this.from="";
        this.to="";
        this.subject="";

        for(Address a : message.getFrom())
            this.from += a.toString() + ", ";
        for(Address a : message.getAllRecipients())
            this.to += a.toString() + ", ";

        this.subject = message.getSubject();
        MimeMessageParser messageParser = new MimeMessageParser((MimeMessage) message);
        messageParser.parse();

        // This block is to check whether mail contains plain text or html entities
        // In case of html, with hasHtmlContent(), hasPlainContent() is also giving true, hence two conditions
        // Also, getPlainContent().isEmpty() is to confirm that mail has html entities,
        if (messageParser.hasPlainContent() && !messageParser.hasHtmlContent()){
            this.content = messageParser.getPlainContent();
        } else {
            if (messageParser.getPlainContent().isEmpty()) {
                this.content = org.jsoup.Jsoup.parse(messageParser.getHtmlContent()).text();
            } else{
                this.content = messageParser.getPlainContent();
            }
        }
        this.date = message.getSentDate();

    }

    @Override
    public String toString() {
        String mailData = String.format("" +
                "To : %s\n" +
                "From : %s\n" +
                "Date : %s\n" +
                "=========================================\n" +
                "Subject : %s\n" +
                "=========================================\n\n" +
                "%s",
                this.to,
                this.from,
                this.date,
                this.subject,
                this.content
        );

        return mailData;
    }
}
