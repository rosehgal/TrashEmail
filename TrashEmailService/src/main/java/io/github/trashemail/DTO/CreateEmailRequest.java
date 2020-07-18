package io.github.trashemail.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateEmailRequest {
    private String source;
    private String destination;
    private String destinationType;
    private String emailId;
    private Boolean isActive;
}
