package io.github.trashemail.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class TrashEmailStats {
    private Long numberOfUsers;
    private Long numberOfEmailsRegistered;
    private Map<String, Long> domainsToNumbers;
    private Long emailIdsCreatedToday;
    private List<Long> emailIdsCreatedInWeek;
    private String version;
    private Long numberOfEmailsProcessed;
    private Long totalNumberOfUsers;

    private List<ConnectorStats> connectorStats;
}
