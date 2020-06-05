package io.github.trashemail.utils;

import io.github.trashemail.Configurations.TrashemailConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import java.io.FileWriter;
import java.util.UUID;

@Component
public class SaveMailToHTMLFile {
    @Autowired
    TrashemailConfig trashemailConfig;

    private static final Logger log = LoggerFactory.getLogger(
            SaveMailToHTMLFile.class);

    public Object saveToFile(String htmlContent){
        try {
            String filename = UUID.randomUUID().toString() + ".html";

            FileWriter myWriter = new FileWriter(
                trashemailConfig.getDownloadPath() + filename);

            myWriter.write(htmlContent);
            myWriter.close();

            log.debug("File written to disk: "+ filename);
            return filename;
        }
        catch (Exception e) {
            log.error("Unable to save to HTML file. " + e.getMessage());
            return null;
        }
    }
}
