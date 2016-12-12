package ch.unibe.ese.team4.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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
import org.springframework.web.servlet.ModelAndView;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
public class PlaceAdControllerTest {	
	
	@Autowired
    private WebApplicationContext wac;
 
    private MockMvc mockMvc;
 
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
	  
	@Test
	public void showingPlaceAdPageTest() throws Exception{
		this.mockMvc.perform(get("/profile/placeAd"))
        			.andExpect(status().isOk())
        			.andExpect(status().is2xxSuccessful())
        			.andExpect(view().name("placeAd"));
	}
	
	@Test
	public void placingAnAdTest() throws Exception{
		this.mockMvc.perform(post("/profile/placeAd")
					.with(user("ese@unibe.ch").password("ese").roles("USER"))
					.param("propertyType", "4")
					.param("sellType", "2")
					.param("city", "3018 - Bern")
					.param("prizeOfSale", "0")
					.param("prize", "0")
					.param("startOffer", "0")
					.param("squareFootage", "0")
					.param("title", "This is a test Ad")
					.param("street", "Hochschulstrasse 6")
					.param("roomDescription", "This is just for test cases")
					.param("moveInDate", "22.12.2020")
					.param("auctionEndDate", "25.12.2020")
					.param("moveOutDate", "25.12.2020")
					)
        			.andExpect(status().isOk())
        			.andExpect(status().is2xxSuccessful())
        			.andExpect(view().name("placeAd"));
	}
	
}