package ch.unibe.ese.team4.controller;

import static org.junit.Assert.assertEquals;
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
public class SearchControllerTest {
	
	@Autowired
	private SearchController searchController;
	
	
	@Autowired
    private WebApplicationContext wac;
 
    private MockMvc mockMvc;
 
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
	  
	@Test
	public void indexTest() throws Exception{
		this.mockMvc.perform(get("/"))
        			.andExpect(status().isOk())
        			.andExpect(view().name("index"));
	}
	
	@Test
	public void searchAdTest(){
		searchController = new SearchController();
		ModelAndView model = searchController.searchAd();
		
		assertEquals("searchAd", model.getViewName());
	}
	
	@Test
	public void testResults() throws Exception{
		this.mockMvc.perform(post("/results")
					.param("buy", "true")
					.param("house", "true")
					.param("city", "3018 - Bern")
					.param("radius", "100")
					.param("prize", "1000"))
					.andExpect(model().hasNoErrors())
					.andExpect(status().isOk())
					.andExpect(forwardedUrl("/pages/results.jsp"))
					.andExpect(view().name("results"));
	}

}
