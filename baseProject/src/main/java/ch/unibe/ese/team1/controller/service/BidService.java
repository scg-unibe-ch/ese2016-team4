package ch.unibe.ese.team1.controller.service;

import java.util.Queue;
import java.util.ArrayList;

import java.util.Date;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.unibe.ese.team1.controller.pojos.forms.SearchForm;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.Alert;
import ch.unibe.ese.team1.model.Bid;
import ch.unibe.ese.team1.model.Message;
import ch.unibe.ese.team1.model.MessageState;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.dao.AdDao;
import ch.unibe.ese.team1.model.dao.AlertDao;
import ch.unibe.ese.team1.model.dao.BidHistoryDao;
import ch.unibe.ese.team1.model.dao.MessageDao;
import ch.unibe.ese.team1.model.dao.UserDao;

import static org.junit.Assert.assertTrue;

@Service
public class BidService {
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
	
	@Autowired
	AdDao adDao;
	
	@Transactional
	public void addBid(long bidAdId, long bidUserId, long bidBid){
		
		Bid newBid = new Bid(bidAdId, bidUserId, bidBid);
		Ad ad = adService.getAdById(newBid.getAdId());
		assertTrue("Ad must be of type auction",ad.getSellType()==3);
		if (ad.isAuctionAvailable() &&
				isHighestBid(newBid) && 
				!isAlreadyHighestBidder(newBid)&&
				!isBidCreator(newBid)){
			User user = userService.findUserById(newBid.getUserId());
			user.getId();
			Queue<Bid> bidHist = new LinkedList<Bid>();
			bidHist.add(newBid);
			Iterator<Bid> bidHistIter = bidHistoryDao.findByAdIdOrderByBidDesc(newBid.getAdId()).iterator();
			while(bidHistIter.hasNext()){
				bidHist.add(bidHistIter.next());
			}
			bidHistoryDao.save(bidHist);
			
			// new: test for alert
			triggerBidAlerts(ad);
		}
		
	}
	
	@Transactional
	public Iterable<Bid> allBids(long adId) {
		List<Bid> adBids = new ArrayList<>();
		for (Bid bidHist : bidHistoryDao.findByAdIdOrderByBidDesc(adId)){
			adBids.add(bidHist);
		}
		return adBids;
	}
	@Transactional
	public String getUserName(long userId) {
		if (userService.findUserById(userId)!=null){
			return userService.findUserById(userId).getUsername();
		}
		return "ghost user";
	}
	
	public boolean isHighestBid(Bid bidHistory){
		Bid bidH = bidHistoryDao.findTop1ByadIdOrderByBidDesc( bidHistory.getAdId() );
		if(bidH == null) return true; 
		return bidHistory.getBid()>bidH.getBid();
	}
	

	public void triggerBidAlerts(Ad ad) {
		Iterable<Bid> bids = bidHistoryDao.findByAdIdOrderByBidDesc(ad.getId());
		
		// send only one message per user, no matter how many alerts were
		// triggered
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
			message.setText(getBidText(ad));
			message.setSender(userDao.findByUsername("System"));
			message.setRecipient(allUsers);
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

	public boolean isAlreadyHighestBidder(Bid bid){
		if (bidHistoryDao.findTop1ByadIdOrderByBidDesc(bid.getAdId())!=null){
			return bidHistoryDao.findTop1ByadIdOrderByBidDesc(bid.getAdId()).getUserId()==bid.getUserId();			
		}
		return false;
	}
	
	public boolean isBidCreator(Bid bid){
		if (adService.getAdById(bid.getAdId())!=null){
			return bid.getUserId() == adService.getAdById(bid.getAdId()).getUser().getId();
		}	
		return false;
	}
	
	public long getMyBid(String username, long adId){
		User user = userService.findUserByUsername(username);
		Iterable<Bid> bids = bidHistoryDao.findByAdIdOrderByBidDesc(adId);
		if (user!=null && bids!=null){
			for(Bid bid : bids){
				if (bid.getUserId()==user.getId()){
					return bid.getBid();
				}
			}
		}
		return -1;
	}
	
	public long getNextBid(long adId){
		long increaseStep = 1; //could be set later in the ad
		Ad ad= adService.getAdById(adId);
		long currentBid = 0;
		long nBid = currentBid + increaseStep;
		if (ad==null){
			return nBid;
		}else if(bidHistoryDao.findTop1ByadIdOrderByBidDesc(adId)==null){
			return nBid < ad.getStartOffer() ? ad.getStartOffer() : nBid;
		}else{
			currentBid = bidHistoryDao.findTop1ByadIdOrderByBidDesc(adId).getBid();
		}
		nBid = currentBid + increaseStep;
		return nBid < ad.getStartOffer() ? ad.getStartOffer() : nBid;
	}
	
	public long getHighestBid(long adId){
		Bid bid = bidHistoryDao.findTop1ByadIdOrderByBidDesc(adId);
		Ad ad= adService.getAdById(adId);
		long currentBid = 0;
		if (ad==null){
			return 0;
		}else if(bid==null){
			return ad.getStartOffer();
		}else{
			currentBid = bid.getBid();
		}
		return currentBid;
	}
	
	public boolean isBidden(long adId){
		return bidHistoryDao.findTop1ByadIdOrderByBidDesc(adId) != null;
	}
	
	public Iterable<Bid> getAllBids(long adId){
		return bidHistoryDao.findByAdIdOrderByBidDesc(adId);
	}
	
}
