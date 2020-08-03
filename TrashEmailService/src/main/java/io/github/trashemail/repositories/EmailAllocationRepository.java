package io.github.trashemail.repositories;

import io.github.trashemail.models.EmailAllocation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface EmailAllocationRepository
        extends CrudRepository<EmailAllocation, Long> {
    EmailAllocation findByEmailIdAndIsActiveTrue(String emailId);
    EmailAllocation findByEmailIdAndDestinationAndDestinationType(String emailId,
                                                                  String destination,
                                                                  String destination_type);
    List<EmailAllocation> findByEmailIdEndsWith(String domain);
    @Query(
            value = "SELECT count(user.emailId) FROM EmailAllocation user " +
                    "WHERE DATE (user.createDateTime) = CURDATE() "
    ) long getEmailIdsCreatedTodayCount();

    /*
    In JPA DB dialects for date manipulations are not good, so using java
    based approach.
    */
    @Query(
            value = "SELECT count(user.emailId) FROM EmailAllocation user " +
                    "WHERE DATE(user.createDateTime) >= :oneWeekOldDate and " +
                    "DATE(user.createDateTime) <= :today " +
                    "GROUP BY DATE(user.createDateTime) " +
                    "ORDER BY DATE(user.createDateTime)"
    ) List<Long> getEmailIdsCreatedInWeek(
            @Param("today") Date currDate,
            @Param("oneWeekOldDate") Date oneWeekOldDate
    );
}
