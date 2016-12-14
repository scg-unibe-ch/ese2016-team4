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

import ch.unibe.ese.team4.controller.service.MessageService;
import ch.unibe.ese.team4.controller.service.UserService;
import ch.unibe.ese.team4.model.Message;
import ch.unibe.ese.team4.model.User;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
public class MessageControllerTest {
	
	@Autowired
    private WebApplicationContext wac;
 
    private MockMvc mockMvc;
    
    @Autowired
    private MessageService messageService;
    
    @Autowired 
    private UserService userService;
    
    private long messageId;
 
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();
        User user = userService.findUserByUsername("ese@unibe.ch");
        Message message = messageService.getInboxForUser(user).iterator().next();
        messageId = message.getId();
    }
	
	@Test
	public void testMessageLoadingPage() throws Exception{
		this.mockMvc.perform(get("/profile/messages")
					.with(user("ese@unibe.ch").password("ese").roles("USER")))
					.andExpect(model().hasNoErrors())
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful())
					.andExpect(forwardedUrl("/pages/messages.jsp"))
					.andExpect(view().name("messages"));
	}
	
	@Test
	public void testGetInbox() throws Exception{
		this.mockMvc.perform(post("/profile/message/inbox")
					.with(user("ese@unibe.ch").password("ese").roles("USER")))
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful());
	}
	
	@Test
	public void testGetSent() throws Exception{
		this.mockMvc.perform(post("/profile/message/sent")
					.with(user("ese@unibe.ch").password("ese").roles("USER")))
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful());
	}

	@Test
	public void testUnread() throws Exception{
		this.mockMvc.perform(get("/profile/unread")
					.with(user("ese@unibe.ch").password("ese").roles("USER")))
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful());
	}
	
	@Test
	public void testSendMessage() throws Exception{
		this.mockMvc.perform(post("/profile/messages/sendMessage")
					.with(user("ese@unibe.ch").password("ese").roles("USER"))
					.param("recipientEmail", "rob.gronkowski@patriots.boston")
					.param("subject", "New contract")
					.param("text", "I need more money"))
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful());
	}
	
	@Test
	public void testMessageSent() throws Exception{
		this.mockMvc.perform(post("/profile/messages")
					.with(user("ese@unibe.ch").password("ese").roles("USER"))
					.param("recipient", "rob.gronkowski@patriots.boston")
					.param("subject", "No")
					.param("text", "Just No"))
					.andExpect(model().hasNoErrors())
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful())
					.andExpect(forwardedUrl("/pages/messages.jsp"))
					.andExpect(view().name("messages"));
	}
	
    @Test
	public void testGetMessage() throws Exception{    	
		this.mockMvc.perform(get("/profile/messages/getMessage")
					.with(user("ese@unibe.ch").password("ese").roles("USER"))
					.param("id", String.valueOf(messageId)))
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful());
	}
    
    @Test
	public void testReadMessage() throws Exception{    	
		this.mockMvc.perform(get("/profile/readMessage")
					.with(user("ese@unibe.ch").password("ese").roles("USER"))
					.param("id", String.valueOf(messageId)))
					.andExpect(status().isOk())
					.andExpect(status().is2xxSuccessful());
	}
}
