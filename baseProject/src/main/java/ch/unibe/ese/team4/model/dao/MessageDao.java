package ch.unibe.ese.team4.model.dao;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.team4.model.Message;
import ch.unibe.ese.team4.model.User;

public interface MessageDao extends CrudRepository<Message, Long> {
	public Iterable<Message> findByRecipient(User user);
	public Iterable<Message> findBySender(User user);
}
