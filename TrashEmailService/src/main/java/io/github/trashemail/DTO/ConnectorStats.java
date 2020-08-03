package io.github.trashemail.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConnectorStats {
    private String connectorName;

    private Long activeUsers;
    private Long totalNumberOfUsers;
    private Long activeEmailIds;
    private Long totalNumberOfEmailIds;
}
