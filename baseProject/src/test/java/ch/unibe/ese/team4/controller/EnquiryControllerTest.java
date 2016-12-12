package ch.unibe.ese.team4.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
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

import ch.unibe.ese.team4.model.dao.UserDao;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
public class EnquiryControllerTest {
	
	@Autowired
    private WebApplicationContext wac;
 
    private MockMvc mockMvc;
    
    //needed to test controllerMethods with param id
    private long userId;
    
    private long userIdOfRatedOne;
    
    @Autowired
    private UserDao userDao;
 
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();
        userId = userDao.findByUsername("ese@unibe.ch").getId();
        userIdOfRatedOne = userDao.findByUsername("jane@doe.com").getId();
    }
	
	@Test
	public void testEnquiriesPage() throws Exception{
		this.mockMvc.perform(get("/profile/enquiries")
					.with(user("ese@unibe.ch").password("ese").roles("USER")))
					.andExpect(model().hasNoErrors())
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful())
					.andExpect(forwardedUrl("/pages/enquiries.jsp"))
					.andExpect(view().name("enquiries"));
	}
	
	@Test
	public void testSendingEnquiry() throws Exception{
		this.mockMvc.perform(get("/profile/enquiries/sendEnquiryForVisit")
					.with(user("ese@unibe.ch").password("ese").roles("USER"))
					.param("id", String.valueOf(userId)))
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful());
	}
	
	@Test
	public void testSavingEnquiry() throws Exception{
		this.mockMvc.perform(get("/profile/enquiries/acceptEnquiry")
					.with(user("ese@unibe.ch").password("ese").roles("USER"))
					.param("id", String.valueOf(userId)))
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful());
	}
	
	@Test
	public void testDeclineEnquiry() throws Exception{
		this.mockMvc.perform(get("/profile/enquiries/declineEnquiry")
					.with(user("ese@unibe.ch").password("ese").roles("USER"))
					.param("id", String.valueOf(userId)))
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful());
	}
	
	@Test
	public void testReopenEnquiry() throws Exception{
		this.mockMvc.perform(get("/profile/enquiries/reopenEnquiry")
					.with(user("ese@unibe.ch").password("ese").roles("USER"))
					.param("id", String.valueOf(userId)))
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful());
	}
	
	@Test
	public void testRateUser() throws Exception{
		this.mockMvc.perform(get("/profile/rateUser")
					.with(user("ese@unibe.ch").password("ese").roles("USER"))
					.param("rate", String.valueOf(userIdOfRatedOne))
					.param("stars", "2"))
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful());
	}
	
	@Test
	public void testRatingForUser() throws Exception{
		this.mockMvc.perform(get("/profile/ratingFor")
					.with(user("ese@unibe.ch").password("ese").roles("USER"))
					.param("user", String.valueOf(userIdOfRatedOne)))
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful());
	}

}
