package ch.unibe.ese.team1.controller.service;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team1.model.Gender;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.UserRole;
import ch.unibe.ese.team1.model.dao.AdDao;
import ch.unibe.ese.team1.model.dao.UserDao;
import ch.unibe.ese.team1.model.dao.VisitDao;
import ch.unibe.ese.team1.model.dao.VisitEnquiryDao;

/**
 * 
 * Tests user create functionality
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})

@WebAppConfiguration
public class CreateUserTest {

	@Autowired
	VisitService visitService;
	
	@Autowired
	EnquiryService enquiryService;
	
	@Autowired
	AdDao adDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	VisitDao visitDao;
	
	@Autowired
	VisitEnquiryDao visitEnquiryDao;
	@Before
	public void setUpMarioUser(){
		//create user and add it
		User superM = createUser("super@mario.ch", "password", "Mario", "N64",
				Gender.MALE);
		superM.setAboutMe("Megafun");
		userDao.save(superM);
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void whenUserExists_thenDataIntegrityViolationException() {		
		//add a new user with the sam mail
		User superM2 = createUser("super@mario.ch", "password", "Mario2", "N64",
				Gender.MALE);
		superM2.setAboutMe("MegafunHigh10");
		userDao.save(superM2);
	}

	
	@Test
	public void createAndDeleteUser() {
		//create user and add it
		assertTrue("user is not created yet",userDao.findByUsername("new@user.ch")==null);
		User superM = createUser("new@user.ch", "password", "New", "Entry",
				Gender.MALE);
		superM.setAboutMe("I'm new");
		userDao.save(superM);
		assertTrue("user in db",userDao.findByUsername("new@user.ch")!=null);
		
		//delete user
		userDao.delete(superM);
		assertTrue("user not in db anymore",userDao.findByUsername("new@user.ch")==null);
	}
	
	@After
	public void deleteUserMario(){
		User mario = userDao.findByUsername("super@mario.ch");
		userDao.delete(mario);
		
	}

	//Lean user creating method
	User createUser(String email, String password, String firstName,
			String lastName, Gender gender) {
		User user = new User();
		user.setUsername(email);
		user.setPassword(password);
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEnabled(true);
		user.setGender(gender);
		Set<UserRole> userRoles = new HashSet<>();
		UserRole role = new UserRole();
		role.setRole("ROLE_USER");
		role.setUser(user);
		userRoles.add(role);
		user.setUserRoles(userRoles);
		return user;
	}
}
