package io.github.trashemail.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.apache.commons.mail.util.MimeMessageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class MailParser {
    private String to;
    private String from;
    private String subject;
    private String content;
    private Date date;
    private String htmlContent;
    private Boolean htmlContentSet;

    private static final Logger log = LoggerFactory.getLogger(
            MailParser.class);

    public MailParser(Message message) throws Exception {
        this.content="";
        this.from="";
        this.to="";
        this.subject="";

        this.htmlContentSet = false;
        this.htmlContent = null;

        for(Address a : message.getFrom())
            this.from += a.toString() + ", ";
        for(Address a : message.getAllRecipients())
            this.to += a.toString() + ", ";

        this.subject = message.getSubject();
        MimeMessageParser messageParser = new MimeMessageParser(
                (MimeMessage) message
        );
        messageParser.parse();

        /*
        Multi-part mime refers to sending both an HTML and TEXT part of
        an email message in a single email. When a subscriber's email client
        receives a multipart message, it accepts the HTML version if it can
        render HTML, otherwise it presents the plain text version.

        So in multipart mails, the content is present in both HTML and plain
        text and its completely on the client capabilities to process which one.

        */

        if(messageParser.isMultipart()){
            /*
            This is the best bet.
            We have both plain content and html content.
            */
            this.htmlContent = messageParser.getHtmlContent();
            this.htmlContentSet = true;
            if(messageParser.getPlainContent().isEmpty()){
                /*
                Plain text segment is present but content is blank.
                 */
                this.content = org.jsoup.Jsoup.parse(
                        messageParser.getHtmlContent()).text();
            }
            else
                this.content = messageParser.getPlainContent();
        }
        else if (messageParser.hasHtmlContent()) {
            this.content = org.jsoup.Jsoup.parse(
                    messageParser.getHtmlContent()).text();
            this.htmlContentSet = true;
            this.htmlContent = messageParser.getHtmlContent();
        }
        else{
            /*
            Incoming mail is just plain text content.
             */
            this.htmlContentSet = false;
            this.content = messageParser.getPlainContent();
        }

        this.date = message.getSentDate();

    }

    @Override
    public String toString() {
        String mailData = String.format("" +
                "To : %s\n" +
                "From : %s\n" +
                "Date : %s\n" +
                "===========================\n" +
                "Subject : %s\n" +
                "===========================\n\n" +
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
