package io.github.trashemail.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SendEmailRequest {
    private String emailId;
    private String message;
}
