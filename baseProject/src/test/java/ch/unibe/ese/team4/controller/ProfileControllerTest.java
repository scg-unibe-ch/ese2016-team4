package ch.unibe.ese.team4.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.team4.controller.service.UserService;

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
    
    //needed to test controllerMethods with param id
    private long userId;
    
    @Autowired
    private UserService userService;
 
    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();
        userId = userService.findUserByUsername("ese@unibe.ch").getId();
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
    				.andExpect(status().is2xxSuccessful())
    				.andExpect(view().name("signup"))
    				.andExpect(forwardedUrl("/pages/signup.jsp"));
	}
	
	@Test
	public void testdoesEmailExists() throws Exception{
		this.mockMvc.perform(post("/signup/doesEmailExist")
					.param("email", "ese@unibe.ch"))
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful());
	}
	
	@Test
	public void testShowingEditProfilePage() throws Exception{
		this.mockMvc.perform(get("/profile/editProfile")
					.with(user("ese@unibe.ch").password("ese").roles("USER")))
					.andExpect(model().hasNoErrors())
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful())
					.andExpect(forwardedUrl("/pages/editProfile.jsp"))
					.andExpect(view().name("editProfile"));
	}
	
	@Test
	public void testShowingGetPremium() throws Exception{
		this.mockMvc.perform(get("/profile/getPremium")
					.with(user("ese@unibe.ch").password("ese").roles("USER")))
					.andExpect(model().hasNoErrors())
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful())
					.andExpect(forwardedUrl("/pages/getPremium.jsp"))
					.andExpect(view().name("getPremium"));
	}
	
	@Test
	public void testShowingUnsubscribePremium() throws Exception{
		this.mockMvc.perform(get("/profile/unsubscribePremium")
					.with(user("ese@unibe.ch").password("ese").roles("USER")))
					.andExpect(model().hasNoErrors())
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful())
					.andExpect(forwardedUrl("/pages/unsubscribePremium.jsp"))
					.andExpect(view().name("unsubscribePremium"));
	}
	
	@Test
	public void testEditProfileResultPage() throws Exception{
		this.mockMvc.perform(post("/profile/editProfile")
					.with(user("ese@unibe.ch").password("ese").roles("USER"))
					.param("username", "peyton.manning@broncos.gov")
					.param("firstName", "Peyton")
					.param("lastName", "Manning")
					.param("password", "broncos4ever"))
					.andExpect(model().hasNoErrors())
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful())
					.andExpect(forwardedUrl("/pages/updatedProfile.jsp"))
					.andExpect(view().name("updatedProfile"));
	}
	
	@Test
	public void testUserGetsPremium() throws Exception{
		this.mockMvc.perform(post("/profile/getPremium")
					.with(user("ese@unibe.ch").password("ese").roles("USER"))
					.param("ccMonth", "11")
					.param("ccYear", "2018")
					.param("ccNumber", "1234567890123456")
					.param("premiumUser", "true"))
					.andExpect(model().hasNoErrors())
					.andExpect(status().isFound())
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/user?id=" + userId))
					.andExpect(view().name("redirect:/user?id="+ userId));
	}
	
	@Test
	public void testUserShouldNotGetPremiumBecauseOfErrors() throws Exception{
		this.mockMvc.perform(post("/profile/getPremium")
					.with(user("ese@unibe.ch").password("ese").roles("USER"))
					.param("ccMonth", "11")
					.param("ccYear", "")
					.param("ccNumber", "1234567890123456")
					.param("premiumUser", "true"))
					.andExpect(model().hasErrors())
					.andExpect(forwardedUrl("/pages/getPremium.jsp"))
					.andExpect(view().name("getPremium"));
	}
	
	@Test
	public void testUserUnsubscribesPremium() throws Exception{
		this.mockMvc.perform(post("/profile/unsubscribePremium")
					.with(user("ese@unibe.ch").password("ese").roles("USER"))
					.param("expensive", "false")
					.param("noUse", "false")
					.param("badService", "false")
					.param("otherReasons", "true"))
					.andExpect(model().hasNoErrors())
					.andExpect(status().isFound())
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/user?id=" + userId))
					.andExpect(view().name("redirect:/user?id="+ userId));
	}
	
	@Test
	public void testUserShouldNotUnsubscribePremiumBecauseOfErrors() throws Exception{
		this.mockMvc.perform(post("/profile/unsubscribePremium")
					.with(user("ese@unibe.ch").password("ese").roles("USER"))
					.param("expensive", "")
					.param("noUse", "")
					.param("badService", "")
					.param("otherReasons", "")
					.param("Reason", "0"))
					.andExpect(model().hasErrors())
					.andExpect(forwardedUrl("/pages/unsubscribePremium.jsp"))
					.andExpect(view().name("unsubscribePremium"));
	}
	
	@Test
	public void showPublicProfileOfUserWithTheGivenId() throws Exception{
		this.mockMvc.perform(get("/user")
					.with(user("ese@unibe.ch").password("ese").roles("USER"))
					.param("id", String.valueOf(userId)))
					.andExpect(model().hasNoErrors())
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful())
					.andExpect(forwardedUrl("/pages/user.jsp"))
					.andExpect(view().name("user"));
	}
	
	@Test
	public void testMethodVisitors() throws Exception{
		this.mockMvc.perform(get("/profile/visitors")
					.with(user("ese@unibe.ch").password("ese").roles("USER"))
					.param("visit", String.valueOf(userId)))
					.andExpect(model().hasNoErrors())
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful())
					.andExpect(view().name("visitors"));
	}
	
	@Test
	public void testLoadingUserSchedulePage() throws Exception{
		this.mockMvc.perform(get("/profile/schedule")
					.with(user("ese@unibe.ch").password("ese").roles("USER")))
					.andExpect(model().hasNoErrors())
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful())
					.andExpect(forwardedUrl("/pages/schedule.jsp"))
					.andExpect(view().name("schedule"));
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
					.andExpect(status().is2xxSuccessful())
					.andExpect(forwardedUrl("/pages/login.jsp"))
					.andExpect(view().name("login"));
	}	
	
	@Test
	public void testAuthenticateG() throws Exception{
		this.mockMvc.perform(get("/authenticateGoogleUser")
					.param("firstName", "Test")
					.param("lastName", "Test")
					.param("userName", "test.test@testes.gov")
					.param("email", "test.test@testes.gov")
					.param("imageURL", "")
					.param("googleId", "1"))
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful());
	}
}
