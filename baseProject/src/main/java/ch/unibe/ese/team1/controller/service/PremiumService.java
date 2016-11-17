package ch.unibe.ese.team1.controller.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import ch.unibe.ese.team1.model.Message;
import ch.unibe.ese.team1.model.dao.MessageDao;

@Configuration
@EnableScheduling
public class PremiumService {
	
	@Autowired
	MessageDao messageDao;
	
	
	
	//static List<Message> messagesToSend = new ArrayLisit<Message>();
	static List<Message> messagesToSend = new ArrayList<Message>();
	
	//Scheduled method for premium support, by handling all delayed task for nonPremium User's in a certain interval
	@Scheduled(cron="*/60 * * * * *")
	public void nonPremiumHandler(){
		List<Message> messagesToDelete = new ArrayList<Message>();
		delayedAlertLoop:
		for(Message message : messagesToSend ){
			Date now = new Date();
			//Date is null if there was a parse problem in AlertService.triggerAlerts() and will *to be changed* be sent directly
			if(message.getDateSent() == null){
				message.setDateSent(now);
				messageDao.save(message);
				messagesToDelete.add(message);
			}
			else if(message.getDateSent().before(now)){
				messageDao.save(message);
				messagesToDelete.add(message);
			}
			else break delayedAlertLoop;
		}

		for(Message msgTD : messagesToDelete){
			messagesToSend.remove(msgTD);
		}
	}
	


	/**
	 *
	 * @param Message which will be saved with a nonPremium delay
	 */
	public static void setMessage(Message msg) {
		PremiumService.messagesToSend.add(msg);
		}
	
	

		
		
		
	

	

}
