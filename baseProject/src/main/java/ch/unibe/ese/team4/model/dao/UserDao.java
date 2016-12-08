package ch.unibe.ese.team4.model.dao;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.team4.model.User;

public interface UserDao extends CrudRepository<User, Long> {
	public User findByUsername(String username);
	
	public User findUserById(long id);

	public User findByGoogleid(String googleId);
}