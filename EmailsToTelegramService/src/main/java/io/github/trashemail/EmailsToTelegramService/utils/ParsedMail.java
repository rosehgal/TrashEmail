package io.github.trashemail.EmailsToTelegramService.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ParsedMail {
    public List<String> recipients;
    public String to;
    public String from;
    public String subject;
    public String content;
    public Date date;
    public String htmlContent;
    public Boolean htmlContentSet;

    @Override
    public String toString() {
        String mailData = String.format(
            "===========================\n" +
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
