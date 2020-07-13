package io.github.trashemail.Respositories;

import io.github.trashemail.models.UsedUserIds;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface UsedUserIdsRepository
        extends CrudRepository<UsedUserIds, Integer> {
    public List<UsedUserIds> findByChatIdAndIsActiveTrue(long chatId);

    public List<UsedUserIds> findAllByEntityNotNullAndDateBetween(
            Date begin, Date end);

}
