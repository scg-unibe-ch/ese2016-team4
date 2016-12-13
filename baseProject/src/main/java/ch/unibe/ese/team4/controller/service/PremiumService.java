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
	
	
	//messages to be sent after the added delay in setMessages()
	private static List<Message> messagesToSend = new ArrayList<Message>();
	
	//sends the messages from the messagesToSend List with the set message.remainingTime delay
	@Scheduled(cron="*/60 * * * * *")
	public void nonPremiumHandler(){
		List<Message> messagesToDelete = new ArrayList<Message>();
		Date now = new Date();

		for(Message message : messagesToSend ){
			if(message.getRemainingTime() <= 0){
				message.setDateSent(now);
				messageDao.save(message);
				messagesToDelete.add(message);
			}
			else{
				int rTime = message.getRemainingTime();
				message.setRemainingTime(rTime-1);
			}
		}

		for(Message msgTD : messagesToDelete){
			messagesToSend.remove(msgTD);
		}
	}

	/**
	 * sets the remaining time in minutes to a message by which it will be delayed and adds the message to messagesToSend List.
	 * @param msg Message which will be sent with a nonPremium delay
	 */
	public static void setMessage(Message msg) {
		msg.setRemainingTime(1);
		
		PremiumService.messagesToSend.add(msg);
		}
	
	/**
	 * sets the remaining time in minutes to a message by which it will be delayed and adds the message to messagesToSend List.
	 * @param msg Message which will be sent with a nonPremium delay
	 * @param delay The nonPremium delay to add
	 */
	public static void setMessageAndDelay(Message msg, int delay){
		msg.setRemainingTime(delay);
		
		PremiumService.messagesToSend.add(msg);
		
	}
	
	public static List<Message> getMessagesToSend(){
		return PremiumService.messagesToSend;
	}
}
