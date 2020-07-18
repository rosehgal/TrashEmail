package io.github.trashemail.EmailsToTelegramService.utils;

import io.github.trashemail.EmailsToTelegramService.Configuration.ImapClientServiceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.util.UUID;

@Component
public class SaveMailToHTMLFile {
    @Autowired
    private ImapClientServiceConfig imapClientServiceConfig;

    private static final Logger log = LoggerFactory.getLogger(
            SaveMailToHTMLFile.class);

    private static String safeHTMLCSPPolicy = "<meta http-equiv=\"Content-Security-Policy\" content=\"default-src 'self';style-src 'unsafe-inline';img-src 'self' data:\">";

    public Object saveToFile(String htmlContent) {
        try {
            String safeHTMLFilename = UUID.randomUUID().toString() + "_safe" + ".html";
            String UnSafeHTMLFilename = UUID.randomUUID().toString() + "_unsafe" + ".html";

            FileWriter myWriter = new FileWriter(
                    imapClientServiceConfig
                            .getEmails()
                            .getDownloadPath() + safeHTMLFilename);
            myWriter.write(safeHTMLCSPPolicy + htmlContent);
            myWriter.close();
            log.debug("File written to disk: " + safeHTMLFilename);

            myWriter = new FileWriter(
                    imapClientServiceConfig
                            .getEmails()
                            .getDownloadPath() + UnSafeHTMLFilename);

            myWriter.write(htmlContent);
            myWriter.close();
            log.debug("File written to disk: " + UnSafeHTMLFilename);

            return new String[]{safeHTMLFilename, UnSafeHTMLFilename};
        } catch (Exception e) {
            log.error("Unable to save to HTML file. " + e.getMessage());
            return null;
        }
    }
}
