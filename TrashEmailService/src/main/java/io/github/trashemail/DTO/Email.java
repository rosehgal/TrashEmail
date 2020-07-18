package io.github.trashemail.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class Email {
    private String emailId;
    private String message;
    private Date arrived;

    public Email(SendEmailRequest sendEmailRequest){
        this.arrived = new Date();
        this.emailId = sendEmailRequest.getEmailId();
        this.message = sendEmailRequest.getMessage();
    }
}
