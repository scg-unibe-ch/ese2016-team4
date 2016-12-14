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

import ch.unibe.ese.team4.controller.service.AdService;
import ch.unibe.ese.team4.controller.service.UserService;
import ch.unibe.ese.team4.model.Ad;
import ch.unibe.ese.team4.model.User;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
public class EditAdControllerTest {
	
	@Autowired
    private WebApplicationContext wac;
 
    private MockMvc mockMvc;
    
    //needed to test controllerMethods with param id
    private long adId;

    @Autowired
    private AdService adService;
    
    @Autowired
    private UserService userService;    
 
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();
        User user = userService.findUserByUsername("ese@unibe.ch");
        Ad ad = adService.getAdsByUser(user).iterator().next();
        adId = ad.getId();
    }
    
    @Test
	public void testGettingEditAdPage() throws Exception{
		this.mockMvc.perform(get("/profile/editAd")
					.with(user("ese@unibe.ch").password("ese").roles("USER"))
					.param("id", String.valueOf(adId)))
					.andExpect(model().hasNoErrors())
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful())
					.andExpect(forwardedUrl("/pages/editAd.jsp"))
					.andExpect(view().name("editAd"));
	}
    
    @Test
	public void testDeleteRoomate() throws Exception{
		this.mockMvc.perform(post("/profile/editAd/deleteRoommate")
					.with(user("ese@unibe.ch").password("ese").roles("USER"))
					.param("adId", "1")
					.param("userId", "6"))
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful());
	}
}
