package io.github.trashemail.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SendEmailRequest {
    private String emailId;
    private String message;
    private String emailURI;
    private String emailDownloadPath;
    private List<String> attachmentsPaths;

    @Override public String toString() {
        return "SendEmailRequest{" +
                "emailId='" + emailId + '\'' +
                ", message='" + message + '\'' +
                ", emailURI='" + emailURI + '\'' +
                ", emailDownloadPath='" + emailDownloadPath + '\'' +
                ", attachmentsPaths=" + attachmentsPaths +
                '}';
    }
}
