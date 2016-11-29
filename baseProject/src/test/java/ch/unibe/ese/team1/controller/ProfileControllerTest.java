package ch.unibe.ese.team1.controller;

import static org.junit.Assert.*;


import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.ModelAndView;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
public class ProfileControllerTest {


	@Autowired
	private ProfileController profileController;
	
	@Test
	public void loadLoginPage() throws Exception{
		profileController = new ProfileController();
		ModelAndView model = profileController.loginPage();
		
		assertEquals("login", model.getViewName());
	}
	
	@Test
	public void loadSignupPage() throws Exception{
		profileController = new ProfileController();
		ModelAndView model = profileController.signupPage();
		
		assertEquals("signup", model.getViewName());
	}
}
