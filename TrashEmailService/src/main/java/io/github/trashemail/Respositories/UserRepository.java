package io.github.trashemail.Respositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.github.trashemail.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>{
	public List<User> findByChatId(long chatId);
	public User findByEmailId(String emailId);
	public boolean existsByEmailId(String emailId);
	public void delete(User user);
	public long count();
	public List<User> findByEmailIdEndsWith(String domain);

	@Query(value = "SELECT count(DISTINCT user.chatId) FROM " +
			"User user")
	public long getDistinctChatIdCount();

	@Query(
			value = "SELECT count(user.emailId) FROM User user " +
			"WHERE DATE (user.createDateTime) = CURDATE() "
	)
	public long getEmailIdsCreatedTodayCount();

	/*
	In JPA DB dialects for date manipulations are not good, so using java
	based approach.
	 */
	@Query(
			value = "SELECT count(user.emailId) FROM User user " +
			"WHERE DATE(user.createDateTime) >= :oneWeekOldDate and " +
					"DATE(user.createDateTime) <= :today " +
			"GROUP BY DATE(user.createDateTime) " +
			"ORDER BY DATE(user.createDateTime)"
	)
	public List<Long> getEmailIdsCreatedInWeek(
			@Param("today") Date currDate,
			@Param("oneWeekOldDate") Date oneWeekOldDate
	);

}
