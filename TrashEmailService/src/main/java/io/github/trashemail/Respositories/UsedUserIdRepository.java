package io.github.trashemail.Respositories;

import io.github.trashemail.models.UsedUserId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UsedUserIdRepository
        extends CrudRepository<UsedUserId, Integer> {
    public List<UsedUserId> findByChatIdAndIsActiveTrue(long chatId);
    public UsedUserId findByUserId(String userId);

    public List<UsedUserId> findAllByCreateDateTimeBetween(
            LocalDateTime begin, LocalDateTime end);

    @Query(value = "SELECT * FROM used_user_ids " +
                    "where create_date_time <= " +
                    "NOW() - INTERVAL 10 MINUTE", nativeQuery = true)
    public List<UsedUserId> getUserIdsCreatedBeforeTenMinutes();
}
