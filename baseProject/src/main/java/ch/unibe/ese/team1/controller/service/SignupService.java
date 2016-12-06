/**
* @Author: Balthasar Hofer <bauz>
* @Date:   2016-12-04T22:31:28+01:00
* @Email:  bauz@gmx.net
* @Last modified by:   bauz
* @Last modified time: 2016-12-06T18:16:00+01:00
*/



package ch.unibe.ese.team1.controller.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;

import ch.unibe.ese.team1.controller.pojos.forms.SignupForm;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.UserRole;
import ch.unibe.ese.team1.model.dao.UserDao;

/** Handles the persisting of new users */
@Service
public class SignupService {

	public static final String DEFAULT_ROLE = "ROLE_USER";

	@Autowired
	private UserDao userDao;

	/** Handles persisting a new user to the database. */
	@Transactional
	public void saveFrom(SignupForm signupForm) {
		User user = new User();
		user.setUsername(signupForm.getEmail());
		user.setEmail(signupForm.getEmail());
		user.setFirstName(signupForm.getFirstName());
		user.setLastName(signupForm.getLastName());
		user.setPassword(signupForm.getPassword());
		user.setEnabled(true);
		user.setGender(signupForm.getGender());

		if (signupForm.isPremiumUser() && signupForm.getCcMonth()!=0 && signupForm.getCcYear()!=0 && signupForm.getCcNumber()!=null) {
			user.setCcMonth(signupForm.getCcMonth());
			user.setCcYear(signupForm.getCcYear());
			user.setCcNumber(signupForm.getCcNumber());
			user.setPremium(true);
		} else
			user.setPremium(false);

		Set<UserRole> userRoles = new HashSet<>();
		UserRole role = new UserRole();
		role.setRole(DEFAULT_ROLE);
		role.setUser(user);
		userRoles.add(role);
		user.setUserRoles(userRoles);

		userDao.save(user);
	}

	/**
	 * Returns whether a user with the given username already exists.
	 *
	 * @param username
	 *            the username to check for
	 * @return true if the user already exists, false otherwise
	 */
	@Transactional
	public boolean doesUserWithUsernameExist(String username) {
		return userDao.findByUsername(username) != null;
	}

	@Transactional
	public boolean doesGoogleUserWithUsernameExist(String username) {
		return true;
	}


}
