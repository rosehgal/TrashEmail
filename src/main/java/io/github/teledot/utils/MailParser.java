package io.github.teledot.utils;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;

public class MailParser {
    private String to;
    private String from;
    private String subject;
    private String content;

    public MailParser(Message message) throws MessagingException, IOException {
        this.content="";
        this.from="";
        this.to="";
        this.subject="";

        for(Address a : message.getFrom())
            this.from += a.toString() + ", ";
        for(Address a : message.getAllRecipients())
            this.to += a.toString() + ", ";

        this.subject = message.getSubject();
        this.content = this.getTextFromMessage(message);

    }

    @Override
    public String toString() {
        String mailData = String.format("" +
                "*------------------------------------------------*\n" +
                "*To* : %s\n" +
                "*From* : %s\n" +
                "*Subject* : *%s*\n" +
                "*------------------------------------------------*\n" +
                "%s",
                this.to,
                this.from,
                this.subject,
                this.content
        );

        return mailData;
    }

    private String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart)  throws MessagingException, IOException{
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break;
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
            }
        }
        return result;
    }
}
