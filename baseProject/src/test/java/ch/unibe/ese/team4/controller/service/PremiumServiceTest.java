package ch.unibe.ese.team4.controller.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


import ch.unibe.ese.team4.model.Message;
import ch.unibe.ese.team4.model.MessageState;
import ch.unibe.ese.team4.model.dao.MessageDao;
import ch.unibe.ese.team4.model.dao.UserDao;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
public class PremiumServiceTest {
	
	@Autowired
	PremiumService premiumService;
	
	@Autowired
	MessageDao messageDao;
	
	@Autowired
	UserDao userDao;
	
	// tests if messages in messagesToSend are sent if the delay is zero or if they stay if delay is >0 
	@Test
	public void addMessageToDelayQueueTest(){
		Message message = new Message();
		Date now = new Date();
		message.setSubject("testmessageTESTMESSAGEtestmessage");
		message.setText("testmessageTESTMESSAGEtestmessage");
		message.setSender(userDao.findByUsername("System"));
		message.setRecipient(userDao.findByUsername("System"));
		message.setState(MessageState.UNREAD);
		message.setDateSent(now);
		
		PremiumService.setMessageAndDelay(message,0);
				
		premiumService.nonPremiumHandler();
		assertFalse(PremiumService.getMessagesToSend().iterator().hasNext());
		
		PremiumService.setMessage(message);
		assertTrue(PremiumService.getMessagesToSend().iterator().hasNext());
		
		premiumService.nonPremiumHandler();
		assertTrue(PremiumService.getMessagesToSend().iterator().hasNext());
	}
}
