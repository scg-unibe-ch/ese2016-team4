package ch.unibe.ese.team1.controller.service;

import java.util.Queue;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.Alert;
import ch.unibe.ese.team1.model.BidHistory;
import ch.unibe.ese.team1.model.Message;
import ch.unibe.ese.team1.model.MessageState;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.dao.AlertDao;
import ch.unibe.ese.team1.model.dao.BidHistoryDao;
import ch.unibe.ese.team1.model.dao.MessageDao;
import ch.unibe.ese.team1.model.dao.UserDao;

import static org.junit.Assert.assertTrue;

public class BidHistoryService {
	@Autowired
	private UserService userService;

	@Autowired
	private AdService adService;
	
	@Autowired
	AlertDao alertDao;
	
	@Autowired
	UserDao userDao;

	@Autowired
	MessageDao messageDao;

	@Autowired
	BidHistoryDao bidHistoryDao;
	
	@Transactional
	public void addBid(BidHistory newBid){
		Ad ad = adService.getAdById(newBid.getAdId());
		assertTrue("Ad must be of type auction",ad.getSellType()==3);
		if (ad.isAuctionAvailable() && isHighestBid(newBid)){
			User user = userService.findUserById(newBid.getUserId());
			user.getId();
			Queue<BidHistory> bidHist = new LinkedList<BidHistory>();
			bidHist.add(newBid);
			Iterator<BidHistory> bidHistIter = bidHistoryDao.findByAdIdOrderByBidDesc(newBid.getAdId()).iterator();
			while(bidHistIter.hasNext()){
				bidHist.add(bidHistIter.next());
			}
			bidHistoryDao.save(bidHist);
			
			// new: test for alert
			triggerBidAlerts(ad);
		}
		
	}
	
	public boolean isHighestBid(BidHistory bidHistory){
		BidHistory bidH = bidHistoryDao.findTop1ByadIdOrderByBidDesc( bidHistory.getAdId() );
		return bidHistory.getBid()>bidH.getBid();
	}
	

	public void triggerBidAlerts(Ad ad) {
		Iterable<BidHistory> bids = bidHistoryDao.findByAdIdOrderByBidDesc(ad.getId());
		
		// send only one message per user, no matter how many alerts were
		// triggered
		List<User> users = new ArrayList<User>();
		for (BidHistory bid : bids) {
			User user = userService.findUserById(bid.getUserId());
			if (!users.contains(user)) {
				users.add(user);
			}
		}

		// send messages to all users with matching alerts
		for (User user : users) {
			Date now = new Date();
			Message message = new Message();
			message.setSubject("Someone placed a bid!");
			message.setText(getBidText(ad));
			message.setSender(userDao.findByUsername("System"));
			message.setRecipient(user);
			message.setState(MessageState.UNREAD);
			message.setDateSent(now);
			messageDao.save(message);
		}
	
		}
		
	private String getBidText(Ad ad) {
		return "Dear user,<br>Another user placed a bid. "
				+ "You can visit it here:<br><br>"
				+ "<a class=\"link\" href=/ad?id="
				+ ad.getId()
				+ ">"
				+ ad.getTitle()
				+ "</a><br><br>"
				+ "Your FlatFindr crew";
	}
	
}
