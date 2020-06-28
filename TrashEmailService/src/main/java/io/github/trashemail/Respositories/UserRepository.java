package io.github.trashemail.Respositories;

import java.util.*;

import io.github.trashemail.utils.DaysAndEmailsCount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.github.trashemail.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>{
	public List<User> findByChatIdAndIsActiveTrue(long chatId);
	public User findByEmailIdAndIsActiveTrue(String emailId);
	public User findByEmailIdAndChatId(String emailId , long chatId);
	public boolean existsByEmailIdAndIsActiveTrue(String emailId);
	public void delete(User user);

	public long count();

	public List<User> findByEmailIdEndsWith(String domain);

	@Query(value = "select max(u.id) from User u")
	public long getNumberOfUsers();

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


	/*
	JPQL doesnot support LIMIT so using page results for limiting the output
	Currently setting page results to 15 days :) in the calling function.
	*/
	@Query(
			value = "SELECT date_format(user.createDateTime, '%Y-%M-%d') as " +
			"day, count(1) as count " +
			"FROM User user " +
			"GROUP BY day ORDER BY day DESC "
	)
	public List<DaysAndEmailsCount> getCommulativeEmailCounts(Pageable pageable);

}
