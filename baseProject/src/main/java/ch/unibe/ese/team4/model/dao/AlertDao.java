package ch.unibe.ese.team4.model.dao;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.team4.model.Alert;
import ch.unibe.ese.team4.model.User;

public interface AlertDao extends CrudRepository<Alert, Long>{

	/** Finds alerts of a given user */
	public Iterable<Alert> findByUser(User user);
	
	/** Find all alerts which price is higher than the price of an ad */
	public Iterable<Alert> findByPriceGreaterThan(int price);
	
}