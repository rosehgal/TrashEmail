package io.github.trashemail.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Email {
    private String emailId;
    private String message;
    private Date arrived;
    private String emailHostingLocation;
    private String emailDownloadPath;
    private List<String> attachmentsPaths;

    public Email(SendEmailRequest sendEmailRequest){
        this.arrived = new Date();
        this.emailId = sendEmailRequest.getEmailId();
        this.message = sendEmailRequest.getMessage();
        this.emailHostingLocation = sendEmailRequest.getEmailHostingLocation();
        this.emailDownloadPath = sendEmailRequest.getEmailDownloadPath();
        this.attachmentsPaths = sendEmailRequest.getAttachmentsPaths();
    }
}
