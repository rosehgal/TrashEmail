package io.github.trashemail.Respositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
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
}
