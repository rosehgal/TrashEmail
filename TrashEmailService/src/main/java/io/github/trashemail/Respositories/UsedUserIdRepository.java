package io.github.trashemail.Respositories;

import io.github.trashemail.models.UsedUserId;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface UsedUserIdRepository
        extends CrudRepository<UsedUserId, Integer> {
    public List<UsedUserId> findByChatIdAndIsActiveTrue(long chatId);
    public UsedUserId findByUserId(String userId);

    public List<UsedUserId> findAllByCreateDateTimeBetween(
            Date begin, Date end);

}
