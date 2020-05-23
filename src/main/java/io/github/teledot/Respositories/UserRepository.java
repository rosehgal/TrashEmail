package io.github.teledot.Respositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.github.teledot.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>{
	public List<User> findByChatId(Integer chatId);
	public User findByEmailId(String emailId);
	public boolean existsByEmailId(String emailId);
	public void deleteUserByEmailId(String emailId);
}
