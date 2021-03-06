package ch.unibe.ese.team4.controller;

import static org.junit.Assert.assertEquals;

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
public class IndexControllerTest {

	@Autowired
	private IndexController indexController;
	
	@Test
	public void aboutTest(){   
		indexController = new IndexController();
		ModelAndView model = indexController.about();
		
		assertEquals("about", model.getViewName());
	}
	
	@Test
	public void disclaimerTest(){
		indexController = new IndexController();
		ModelAndView model = indexController.disclaimer();
		
		assertEquals("disclaimer", model.getViewName());
	}

}
