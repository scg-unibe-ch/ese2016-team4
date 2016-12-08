package ch.unibe.ese.team4.controller.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import ch.unibe.ese.team4.model.Message;
import ch.unibe.ese.team4.model.dao.MessageDao;

/**
 * handles all schedules for nonPremium delays
*/
@Configuration
@EnableScheduling
public class PremiumService {
	
	@Autowired
	MessageDao messageDao;
	
	
	//messages to be sent after the added delay in AlertService.triggerAlerts
	static List<Message> messagesToSend = new ArrayList<Message>();
	
	//handles delayed messages for nonPremium User's in a certain interval
	@Scheduled(cron="*/60 * * * * *")
	public void nonPremiumHandler(){
		List<Message> messagesToDelete = new ArrayList<Message>();
		Date now = new Date();
		delayedAlertLoop:
		for(Message message : messagesToSend ){
			//Date is null if there was a parse problem in AlertService.triggerAlerts() and will be sent directly
			if(message.getDateSent() == null){
				message.setDateSent(now);
				messageDao.save(message);
				messagesToDelete.add(message);
			}
			else if(message.getDateSent().after(now)){
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
	 * @param msg Message which will be sent with a nonPremium delay
	 */
	public static void setMessage(Message msg) {
		PremiumService.messagesToSend.add(msg);
		}


}
