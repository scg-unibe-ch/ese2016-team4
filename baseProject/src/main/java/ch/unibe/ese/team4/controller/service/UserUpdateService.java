package ch.unibe.ese.team4.controller.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team4.controller.pojos.forms.EditProfileForm;
import ch.unibe.ese.team4.controller.pojos.forms.GetPremiumForm;
import ch.unibe.ese.team4.controller.pojos.forms.UnsubscribePremiumForm;
import ch.unibe.ese.team4.model.User;
import ch.unibe.ese.team4.model.dao.UserDao;

/** Handles updating a user's profile. */
@Service
public class UserUpdateService {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private UserDao userDao;

	@Autowired
	private UserService userService;

	/** Handles updating an existing user in the database. */
	@Transactional
	public void updateFrom(EditProfileForm editProfileForm, String username) {
		
		User currentUser = userService.findUserByUsername(username);
		
		currentUser.setFirstName(editProfileForm.getFirstName());
		currentUser.setLastName(editProfileForm.getLastName());
		currentUser.setPassword(editProfileForm.getPassword());
		currentUser.setAboutMe(editProfileForm.getAboutMe());
		currentUser.setEmail(editProfileForm.getUsername());

		userDao.save(currentUser);
	}
	
	/** Handles updating an existing user in the database. */
	@Transactional
	public void updateFrom(GetPremiumForm getPremiumForm, String username) {
		
		User currentUser = userService.findUserByUsername(username);

		currentUser.setPremium(true);
		userDao.save(currentUser);
	}
	
	/** Handles updating an existing user in the database. */
	@Transactional
	public void updateFrom(UnsubscribePremiumForm unsubscribePremiumForm, String username) {
		
		User currentUser = userService.findUserByUsername(username);

		currentUser.setPremium(false);
		userDao.save(currentUser);
	}

	
	
}
