package ch.unibe.ese.team4.controller.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team4.model.Ad;
import ch.unibe.ese.team4.model.Bid;
import ch.unibe.ese.team4.model.Message;
import ch.unibe.ese.team4.model.MessageState;
import ch.unibe.ese.team4.model.User;
import ch.unibe.ese.team4.model.dao.AdDao;
import ch.unibe.ese.team4.model.dao.BidHistoryDao;
import ch.unibe.ese.team4.model.dao.MessageDao;
import ch.unibe.ese.team4.model.dao.UserDao;

@Service
public class AuctionService {
	
	@Autowired
    private AdDao adDao;
	
	@Autowired
    private BidHistoryDao bidHistoryDao;

	@Autowired
    private UserDao userDao;
	    
	@Autowired
	private MessageDao messageDao;

	@Autowired
	private UserService userService;
	
	@Autowired
	private AdService adService;
	
	@Autowired
	private BidService bidService;

	
	/** Checks for finished auction */
	@Transactional
	@Scheduled(fixedRate = 10000)
	public void checkForFinishedAuctions(){   
		Iterable<Ad> finishedAuctions = adDao.findByAuctionEndDateLessThanAndAuctionFinished(new Date(),false);
        for(Ad ad : finishedAuctions){
            ad.setFinished(true);
            adDao.save(ad);
            if(bidHistoryDao.findTop1ByadIdOrderByBidDesc(ad.getId()) != null){
        		Bid highestBid = bidHistoryDao.findTop1ByadIdOrderByBidDesc(ad.getId());
        		User user = userService.findUserById(highestBid.getUserId());
            	successWithAuctionMessage(ad);
            	successWithAuctionMessage(ad, user);
            }else{
                noSuccessWithAuctionMessage(ad);
            }
        }
		
	}
	
	/**
     * Sets expired to true
     * Sends message to winner
     * @param id of ad
     */
    @Transactional
    public void instantBuy(long id, User winner){
        // Mark as expired
        Ad ad = adService.getAdById(id);
        ad.setFinished(true);
        instantBuyMessage(ad, winner);
        purchaseCompleteMessage(ad, winner);
        adDao.save(ad);
      }
	
    private void instantBuyMessage(Ad ad, User winner) {
    	
    	User user = ad.getUser();
    	Date now = new Date();
		Message message = new Message();
		message.setSubject("Purchase Confirmation");
		message.setText(getInstantBuyMessage(ad, winner));
		message.setSender(userDao.findByUsername("System"));
		message.setRecipient(user);
		message.setState(MessageState.UNREAD);
		message.setDateSent(now);
		messageDao.save(message);
    	
    }
    
    private void purchaseCompleteMessage(Ad ad, User winner) {
    	Date now = new Date();
		Message message = new Message();
		message.setSubject("Congratulations!");
		message.setText(getPurchaseCompleteMessage(ad, winner));
		message.setSender(userDao.findByUsername("System"));
		message.setRecipient(winner);
		message.setState(MessageState.UNREAD);
		message.setDateSent(now);
		messageDao.save(message);
    }
    
    private String getInstantBuyMessage(Ad ad, User winner) {
    	User user = ad.getUser();
    	
    	return "Dear " + user.getFirstName() 
    			+ ",<br><br> You just sold your:"
				+ "<br><a class=\"link\" href=/ad?id="
				+ ad.getId()
				+ ">"
				+ ad.getTitle()
				+ "</a>"
				+" to " + winner.getFirstName()+ " " + winner.getLastName() + " for "+ ad.getInstantBuyPrice() + " CHF."
				+ "<br><br>We hope you will continue using PropertyFindr."
				+ "<br><br>"
				+ "Your PropertyFindr crew";
    }
    
    private String getPurchaseCompleteMessage(Ad ad, User winner) {
    	User user = ad.getUser();
    	return "Dear " + winner.getFirstName()
    			+ ", <br><br> Congratulations! You just bought:"
				+ "<br><br><a class=\"link\" href=/ad?id="
				+ ad.getId()
				+ ">"
				+ ad.getTitle()
				+ "</a>"
				+" from " + user.getFirstName()+ " " + user.getLastName() + " for "+ ad.getInstantBuyPrice() + " CHF."
				+ "<br><br>He will contact you soon. If you have any questions do not hesitate to contact us!"
				+ "<br>We hope you will continue using PropertyFindr."
				+ "<br><br>"
				+ "Your PropertyFindr crew";
    }
    
	/** 
	 * Sends a message to the owner of an ad when the auction was successful.
	 * 
	 * @param ad The ad to check whether the auction was successful
	 */
	private void successWithAuctionMessage(Ad ad){
        User user = ad.getUser();
		
        Date now = new Date();
		Message message = new Message();
		message.setSubject("Auction expired successfully!");
		message.setText(getSuccessWithAuctionMessage(ad));
		message.setSender(userDao.findByUsername("System"));
		message.setRecipient(user);
		message.setState(MessageState.UNREAD);
		message.setDateSent(now);
		messageDao.save(message);
	}
	
	private void successWithAuctionMessage(Ad ad, User user) {
		Date now = new Date();
		Message message = new Message();
		message.setSubject("Congratulations");
		message.setText(getAuctionWinnerMessage(ad));
		message.setSender(userDao.findByUsername("System"));
		message.setRecipient(user);
		message.setState(MessageState.UNREAD);
		message.setDateSent(now);
		messageDao.save(message);
	}
	/** 
	 * Sends a message to the owner of an ad when the auction was not successful.
	 * 
	 * @param ad The ad to check whether the auction was successful
	 */
	private void noSuccessWithAuctionMessage(Ad ad){
		User user = ad.getUser();
		
        Date now = new Date();
		Message message = new Message();
		message.setSubject("Auction expired unsuccessfully!");
		message.setText(getNoSuccessWithAuctionMessage(ad));
		message.setSender(userDao.findByUsername("System"));
		message.setRecipient(user);
		message.setState(MessageState.UNREAD);
		message.setDateSent(now);
		messageDao.save(message);

	}
	
	// Message that is sent when the auction was not successful
	public String getNoSuccessWithAuctionMessage(Ad ad) {
    	User user = ad.getUser();

		return "Dear " + user.getFirstName() 
				+ ",<br><br>Your auction has ended. Unfortunately no one placed a bid on your advertisement: <br><br>"
				+ "<a class=\"link\" href=/ad?id="
				+ ad.getId()
				+ ">"
				+ ad.getTitle()
				+ "</a><br><br>"
				+ "We hope you will continue using PropertyFindr.<br><br>"
				+ "Your PropertyFindr crew";
	}
	
	public String getAuctionWinnerMessage(Ad ad) {
    	User user = ad.getUser();

		return "Dear " + user.getFirstName() 
				+ ",<br><br>Congratulations! You were the highest bidder and therefore win the following property:<br><br>"
				+ "<a class=\"link\" href=/ad?id="
				+ ad.getId()
				+ ">"
				+ ad.getTitle()
				+ "</a><br><br>"
				+ "The owner will soon contact you.<br><br>"
				+ "Your PropertyFindr crew";
		
	}
	
	// Message that is sent when the auction was successful
	public String getSuccessWithAuctionMessage(Ad ad) {
    	User user = ad.getUser();

		return "Dear " + user.getFirstName() 
				+ ",<br><br>Congratulations! You found a buyer for your: <br><br>"
				+ "<a class=\"link\" href=/ad?id="
				+ ad.getId()
				+ ">"
				+ ad.getTitle()
				+ "</a><br><br>"
				+ "We hope you will continue using PropertyFindr."
				+ "<br><br>Your PropertyFindr crew";	
	}
	

	
	/**
	 * Triggers all bids for a given ad to check if a user has been overbid. 
	 * For every user, only one message is sent.
	 */
	public void triggerBids(Ad ad) {
		Iterable<Bid> bids = bidHistoryDao.findByAdIdOrderByBidDesc(ad.getId());
		List<User> users = new ArrayList<User>();
		for (Bid bid : bids) {
			User user = userService.findUserById(bid.getUserId());
			if(users.isEmpty() ) {
				users.add(user);
			}else if (user.getId() != users.get(0).getId()){
				users.add(user);
			}
		}
		users.remove(0);
			
		for (User allUsers : users) {
			Date now = new Date();
			Message message = new Message();
			message.setSubject("Someone placed a bid!");
			message.setText(getBidText(ad, allUsers));
			message.setSender(userDao.findByUsername("System"));
			message.setRecipient(allUsers);
			message.setState(MessageState.UNREAD);
			message.setDateSent(now);
			messageDao.save(message);
		}
	}
	
	
	// Message that is sent when a user has been overbid
	private String getBidText(Ad ad, User allUsers) {

		return "Dear " + allUsers.getFirstName() + ",<br><br>Another user placed a bid. "
				+ "You can visit it here:<br><br>"
				+ "<a class=\"link\" href=/ad?id="
				+ ad.getId()
				+ ">"
				+ ad.getTitle()
				+ "</a><br><br>"					
				+ "Your PropertyFindr crew";
		}

}
