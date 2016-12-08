package ch.unibe.ese.team4.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.team4.controller.ProfileController;
import ch.unibe.ese.team4.controller.pojos.forms.SignupForm;
import ch.unibe.ese.team4.model.Gender;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
public class ProfileControllerTest {


	@Autowired
	private ProfileController profileController;
	
	@Autowired
    private WebApplicationContext wac;
 
    private MockMvc mockMvc;
 
    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
	
	@Test
	public void testLoadLoginPage() throws Exception{
		profileController = new ProfileController();
		ModelAndView model = profileController.loginPage();
		
		assertEquals("login", model.getViewName());
	}
	
	@Test
	public void testLoadSignupPage() throws Exception{
		this.mockMvc.perform(get("/signup"))
    				.andExpect(status().isOk())
    				.andExpect(view().name("signup"))
    				.andExpect(forwardedUrl("/pages/signup.jsp"));
	}
	
	@Test
	public void testValidSignUpForm() throws Exception{
		this.mockMvc.perform(post("/signup")
					.param("firstName", "Peyton")
					.param("lastName", "Manning")
					.param("email", "peyton.manning@broncos.gov")
					.param("password", "broncos4ever")
					.param("gender", "MALE"))
					.andExpect(model().hasNoErrors())
					.andExpect(status().isOk())
					.andExpect(forwardedUrl("/pages/login.jsp"))
					.andExpect(view().name("login"));
	}
	
	@Test
	public void testInvalidSignUpForm() throws Exception{
		this.mockMvc.perform(post("/signup")
				.param("firstName", "")
				.param("lastName", "")
				.param("email", "")
				.param("password", "")
				.param("gender", "MALE"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("/pages/login.jsp"))
				.andExpect(model().attributeHasFieldErrors("signupForm", "firstName"))
				.andExpect(model().attributeHasFieldErrors("signupForm", "lastName"))
				.andExpect(model().attributeHasFieldErrors("signupForm", "email"))
				.andExpect(model().attributeHasFieldErrors("signupForm", "password"))
				.andExpect(view().name("signup"));
	}
}
