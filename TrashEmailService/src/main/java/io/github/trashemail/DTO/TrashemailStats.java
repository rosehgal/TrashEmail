package io.github.trashemail.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class TrashemailStats {
    private Long numberOfUsers;
    private Long numberOfEmailsRegistered;
    private Map<String, Long> domainsToNumbers;
    private Boolean applicationStatus;
    private Boolean dbConnection;
}
