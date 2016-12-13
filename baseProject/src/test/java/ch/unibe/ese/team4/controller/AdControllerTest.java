package ch.unibe.ese.team4.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
import ch.unibe.ese.team4.controller.service.BookmarkService;
import ch.unibe.ese.team4.model.Ad;
import ch.unibe.ese.team4.model.User;
import ch.unibe.ese.team4.model.dao.AdDao;
import ch.unibe.ese.team4.model.dao.UserDao;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
public class AdControllerTest {
	
	@Autowired
    private WebApplicationContext wac;
 
    private MockMvc mockMvc;
    //needed to test controllerMethods with param id
    private long adId;

    @Autowired
    private AdDao adDao;
    @Autowired
    private UserDao userDao;
	@Autowired
	private BookmarkService bookmarkService;
	@Autowired
	private AdService adService;
    
 
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();
        Iterable<Ad> ads = adDao.findAll();
        adId=-1; 
        for(Ad a : ads){
        	if (a.getSellType()==Ad.AUCTION){
        		adId = a.getId();
        		break;
        	}
        }
    }
    
    @Test
	public void testMyRooms() throws Exception{
		this.mockMvc.perform(get("/profile/myRooms")
					.with(user("ese@unibe.ch").password("ese").roles("USER")))
					.andExpect(model().hasNoErrors())
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful())
					.andExpect(forwardedUrl("/pages/myRooms.jsp"))
					.andExpect(view().name("myRooms"));
	}
    
    @Test
	public void testAd() throws Exception{    	
		this.mockMvc.perform(get("/ad")
					.with(user("ese@unibe.ch").password("ese").roles("USER"))
					.param("id", String.valueOf(adId)))
					.andExpect(model().hasNoErrors())
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful())
					.andExpect(view().name("adDescription"));
	}
    
    @Test
	public void testMethodMessageSent() throws Exception{    	
		this.mockMvc.perform(post("/ad")
					.with(user("ese@unibe.ch").password("ese").roles("USER"))
					.param("id", String.valueOf(adId))
					.param("bid", "1000")
					.param("recipient", "TestUser")
					.param("subject", "testCase")
					.param("text", "testText"))
					.andExpect(model().hasNoErrors())
					.andExpect(status().isFound())
					.andExpect(status().is3xxRedirection())
					.andExpect(view().name("redirect:/ad?id="+String.valueOf(adId)));
	}
    
    @Test
	public void testIsBookmarked() throws Exception{
    	// when ad is not bookmarked and button "bookmark" is hit, ad should be marked!
    	boolean bookmarked = false; //ad is currently not bookmarked
    	this.mockMvc.perform(post("/bookmark")
					.with(user("jane@doe.com").password("password").roles("USER"))
					.param("id", String.valueOf(adId))
					.param("screening", String.valueOf(false))
					.param("bookmarked",String.valueOf(bookmarked)))
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful());
    	User jane = userDao.findByUsername("jane@doe.com");
    	boolean isBookmarked = false;
    	for (Ad a : jane.getBookmarkedAds()){
    		if (a.getId() == adId){
    			isBookmarked = true;
    			break;
    		}
    	}
    	assertTrue(isBookmarked);

    	// when ad is bookmarked and button "bookmark" is hit, ad should be unmarked!
    	bookmarked = true; //ad is already bookmarked
    	this.mockMvc.perform(post("/bookmark")
					.with(user("jane@doe.com").password("password").roles("USER"))
					.param("id", String.valueOf(adId))
					.param("screening", String.valueOf(false))
					.param("bookmarked",String.valueOf(bookmarked)))
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful());
    	isBookmarked = false;
    	jane = userDao.findByUsername("jane@doe.com");
    	for (Ad a : jane.getBookmarkedAds()){
    		if (a.getId() == adId){
    			isBookmarked = true;
    			break;
    		}
    	}
    	assertFalse(isBookmarked);
	}

}
