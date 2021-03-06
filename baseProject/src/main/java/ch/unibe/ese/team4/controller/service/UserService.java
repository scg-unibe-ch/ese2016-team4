package ch.unibe.ese.team4.controller.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team4.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team4.controller.pojos.forms.UnsubscribePremiumForm;
import ch.unibe.ese.team4.model.Ad;
import ch.unibe.ese.team4.model.User;
import ch.unibe.ese.team4.model.dao.UserDao;

/**
 * Handles all database actions concerning users.
 */
@Service
public class UserService {
	
	@Autowired
	private UserDao userDao;

	/** Gets the user with the given username. */
	@Transactional
	public User findUserByUsername(String username) {
		return userDao.findByUsername(username);
	}
	
	/** Gets the user with the given id. */
	@Transactional
	public User findUserById(long id) {
		return userDao.findUserById(id);
	}

	public User findUserByGoogleId(String googleId) {
		return userDao.findByGoogleid(googleId);
	}
	
}
