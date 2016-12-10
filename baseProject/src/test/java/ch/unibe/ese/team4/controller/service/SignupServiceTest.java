package ch.unibe.ese.team4.controller.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team4.controller.ProfileController;
import ch.unibe.ese.team4.controller.pojos.forms.SignupForm;
import ch.unibe.ese.team4.model.Gender;
import ch.unibe.ese.team4.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
public class SignupServiceTest {
	@Autowired
	private SignupService signupService;
	
	@Autowired
	private UserService userService;	
	
	@Test
	public void saveUserWithPremium(){
		SignupForm signupForm = new SignupForm();
		signupForm.setFirstName("Max");
		signupForm.setLastName("Muster");
		signupForm.setGender(Gender.MALE);
		signupForm.setEmail("max@muster.de");
		signupForm.setPassword("maxmuster");
		signupForm.setPremiumUser(true);
		signupForm.setCcMonth(2);
		signupForm.setCcYear(2020);
		signupForm.setCcNumber("1234123412341234");
		
		signupService.saveFrom(signupForm);
		
		User user = userService.findUserByUsername("max@muster.de");
		
		//Testing
		assertEquals("Max", user.getFirstName());
		assertEquals(2, user.getCcMonth());
		assertEquals(2020, user.getCcYear());
		assertEquals("1234123412341234", user.getCcNumber());
		assertTrue(user.isPremium());

	}
	
	@Test
	public void saveUserWithoutPremium(){
		SignupForm signupForm = new SignupForm();
		signupForm.setFirstName("Test");
		signupForm.setLastName("Testmann");
		signupForm.setGender(Gender.FEMALE);
		signupForm.setEmail("test@test.de");
		signupForm.setPassword("password");
		signupForm.setPremiumUser(true);
		signupForm.setCcMonth(0);
		signupForm.setCcYear(0);
		signupForm.setCcNumber("");
		
		signupService.saveFrom(signupForm);
		
		User user = userService.findUserByUsername("test@test.de");
		
		//Testing
		assertEquals("Test", user.getFirstName());
		assertEquals(0, user.getCcMonth());
		assertEquals(0, user.getCcYear());
		assertEquals(null, user.getCcNumber());
		//false because user didn't enter a creditcard, even though premium user was checked
		assertFalse(user.isPremium());

	}
	

	@Test
	public void testSaveGoogleUser(){
		User user = signupService.signupGoogleUser("ese@unibe.ch", "ese", "John", "Wayne",
				"/img/test/portrait.jpg", Gender.MALE, true, "");
		
		assertEquals("ese@unibe.ch", user.getEmail());
		assertEquals("ese", user.getPassword());
		assertEquals("John", user.getFirstName());
		assertEquals("Wayne", user.getLastName());
		assertEquals("", user.getGoogleId());
		assertEquals("1111222233334444", user.getCcNumber());
		assertEquals(5 , user.getCcMonth());
		assertEquals(2020, user.getCcYear());
	}
}
