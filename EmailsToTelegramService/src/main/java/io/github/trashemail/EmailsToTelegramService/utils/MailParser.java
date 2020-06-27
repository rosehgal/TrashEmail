package io.github.trashemail.EmailsToTelegramService.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.mail.util.MimeMessageParser;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@Component
public class MailParser {
    private static final Logger log = LoggerFactory.getLogger(
            MailParser.class);

    public ParsedMail parseMail(Message message) throws Exception {
        ParsedMail parsedMail = new ParsedMail();

        parsedMail.content = "";
        parsedMail.from = "";
        parsedMail.to = "";
        parsedMail.recipients = new ArrayList<>();
        parsedMail.subject = "";

        parsedMail.htmlContentSet = false;
        parsedMail.htmlContent = null;
        parsedMail.attachmentSet = false;
        parsedMail.attachmentList = new ArrayList<>();

        for (Address a : message.getFrom())
            parsedMail.from += a.toString() + ", ";

        parsedMail.subject = message.getSubject();
        MimeMessageParser messageParser = new MimeMessageParser(
                (MimeMessage) message
        );
        /*
        Parse the message object.
         */
        messageParser.parse();

        /*
        Get the list of To and put it in recipients
         */
        for (Address a : messageParser.getTo()) {
            parsedMail.getRecipients().add(
                    ((InternetAddress) a).getAddress()
            );
            parsedMail.to += a.toString() + ", ";
        }
        /*
        Get the list of CC mail addresses and put in recipients
        */
        for (Address a : messageParser.getCc()) {
            parsedMail.getRecipients().add(
                    ((InternetAddress) a).getAddress()
            );
            parsedMail.to += a.toString() + ", ";
        }

        /*
        Get the list of BCC mail addresses and put in recipients
        */
        for (Address a : messageParser.getBcc()) {

            log.debug("BCC mail for " + a.toString());

            parsedMail.getRecipients().add(
                    ((InternetAddress) a).getAddress()
            );
            parsedMail.to += a.toString() + ", ";
        }


        /*
        Multi-part mime refers to sending both an HTML and TEXT part of
        an email message in a single email. When a subscriber's email client
        receives a multipart message, it accepts the HTML version if it can
        render HTML, otherwise it presents the plain text version.

        So in multipart mails, the content is present in both HTML and plain
        text and its completely on the client capabilities to process which one.

        */

        if (messageParser.isMultipart()) {
            if (messageParser.hasAttachments()) {
                /*
                We have all plain content, attachments and html content.
                */
                parsedMail.attachmentSet = true;

                for (DataSource data : messageParser.getAttachmentList()) {
                    String nameRelativeFile = "/tmp" + File.separator + data.getName();
                    parsedMail.attachmentList.add(nameRelativeFile);
                    boolean ret = FileHelper.writeFile(data.getInputStream(), nameRelativeFile);
                    if (ret) {
                        log.debug("Attachment file written successfully at location: " + nameRelativeFile);
                    }
                }
            }
            /*
            This is the best bet.
            We have both plain content and html content without attachments.
            */
            parsedMail.htmlContent = messageParser.getHtmlContent();
            parsedMail.htmlContentSet = true;
            if (messageParser.getPlainContent().isEmpty()) {
                /*
                Plain text segment is present but content is blank.
                 */
                parsedMail.content = Jsoup.parse(
                        messageParser.getHtmlContent()).text();
            } else
                parsedMail.content = messageParser.getPlainContent();
        } else if (messageParser.hasHtmlContent()) {
            parsedMail.content = Jsoup.parse(
                    messageParser.getHtmlContent()).text();
            parsedMail.htmlContentSet = true;
            parsedMail.htmlContent = messageParser.getHtmlContent();
        } else {
            /*
            Incoming mail is just plain text content.
             */
            parsedMail.htmlContentSet = false;
            parsedMail.content = messageParser.getPlainContent();
        }

        parsedMail.date = message.getSentDate();
        return parsedMail;
    }

}

