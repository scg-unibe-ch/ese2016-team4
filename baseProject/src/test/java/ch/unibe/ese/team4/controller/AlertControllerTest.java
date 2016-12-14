package ch.unibe.ese.team4.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import ch.unibe.ese.team4.controller.service.AlertService;
import ch.unibe.ese.team4.controller.service.UserService;
import ch.unibe.ese.team4.model.Alert;
import ch.unibe.ese.team4.model.User;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
public class AlertControllerTest {
	
	@Autowired
    private WebApplicationContext wac;
 
    private MockMvc mockMvc;

    @Autowired
    private AlertService alertService;
    
    @Autowired
    private UserService userService;
    
    private long alertId;
 
    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();
        User user = userService.findUserByUsername("ese@unibe.ch");
        Alert alert = alertService.getAlertsByUser(user).iterator().next();
        alertId = alert.getId();
    }
    
    @Test
    public void testGetAlertPage() throws Exception{
    	this.mockMvc.perform(get("/profile/alerts")
    				.with(user("ese@unibe.ch").password("ese").roles("USER"))
    				.param("buy", "true")
    				.param("house", "true")
    				.param("city", "3018 - Bern")
    				.param("radius", "100")
    				.param("prize", "1000")
    				.param("squareFootage", "5")
    				.param("alreadySet", "true"))
    				.andExpect(model().hasNoErrors())
    				.andExpect(status().isOk())
    				.andExpect(status().is2xxSuccessful())
    				.andExpect(forwardedUrl("/pages/alerts.jsp"))
    				.andExpect(view().name("alerts"));
    }	
    
    @Test
    public void testSaveAlert() throws Exception{
    	this.mockMvc.perform(post("/profile/alerts")
    				.with(user("ese@unibe.ch").password("ese").roles("USER"))
    				.param("buy", "true")
    				.param("house", "true")
    				.param("city", "3018 - Bern")
    				.param("radius", "100")
    				.param("prize", "1000")
    				.param("squareFootage", "5")
    				.param("alreadySet", "true")
    				.param("propertyType", "4")
    				.param("sellType", "2")
    				.param("moveInDate", "11.12.2020")
    				.param("moveOutDate", "11.12.2020"))
    				.andExpect(model().hasNoErrors())
    				.andExpect(status().isOk())
    				.andExpect(status().is2xxSuccessful())
    				.andExpect(forwardedUrl("/pages/alerts.jsp"))
    				.andExpect(view().name("alerts"));
    }
    
    @Test
    public void testDeleteAlert() throws Exception{
    	this.mockMvc.perform(get("/profile/alerts/deleteAlert")
    				.with(user("ese@unibe.ch").password("ese").roles("USER"))
    				.param("id", String.valueOf(alertId)))
    				.andExpect(status().isOk())
    				.andExpect(status().is2xxSuccessful());
    }
}
