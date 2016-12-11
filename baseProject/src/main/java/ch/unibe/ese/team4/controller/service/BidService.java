package ch.unibe.ese.team4.controller.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.unibe.ese.team4.model.Ad;
import ch.unibe.ese.team4.model.Bid;
import ch.unibe.ese.team4.model.User;
import ch.unibe.ese.team4.model.dao.AdDao;
import ch.unibe.ese.team4.model.dao.AlertDao;
import ch.unibe.ese.team4.model.dao.BidHistoryDao;
import ch.unibe.ese.team4.model.dao.MessageDao;
import ch.unibe.ese.team4.model.dao.UserDao;

@Service
public class BidService {
	@Autowired
	private UserService userService;

	@Autowired
	private AdService adService;
	
	@Autowired
	private AuctionService auctionService;
	
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
	
	/**
	 * Adds a new bid to an ad.
	 * 
	 * @param bidAdId
	 * 			the id of the new bid
	 * @param bidUserId
	 * 			the id of the user that placed the bid
	 * @param bidBid
	 * 			the amount/value of the bid
	 */
	
	@Transactional
	public void addBid(long bidAdId, long bidUserId, long bidBid){
		
		Bid newBid = new Bid(bidAdId, bidUserId, bidBid);
		Ad ad = adService.getAdById(newBid.getAdId());
		assertTrue("Ad must be of type auction",ad.getSellType()==3);
		if (ad.isAuctionAvailable() &&
				isHighestBid(newBid) && 
				!isAlreadyHighestBidder(newBid)&&
				!isBidCreator(newBid)&&
				ad.getStartOffer()<=(int)bidBid){
			User user = userService.findUserById(newBid.getUserId());
			user.getId();
			Queue<Bid> bidHist = new LinkedList<Bid>();
			bidHist.add(newBid);
			Iterator<Bid> bidHistIter = bidHistoryDao.findByAdIdOrderByBidDesc(newBid.getAdId()).iterator();
			while(bidHistIter.hasNext()){
				bidHist.add(bidHistIter.next());
			}
			bidHistoryDao.save(bidHist);
			
			auctionService.triggerBids(ad);
		}
		
	}
	
	/**
	 * Returns all bids that belong to a given ad.
	 * 
	 * @param adId
	 * 			the ad to get the bids from
	 * @return
	 * 			a list of bids
	 */
	
	@Transactional
	public Iterable<Bid> allBids(long adId) {
		List<Bid> adBids = new ArrayList<>();
		for (Bid bidHist : bidHistoryDao.findByAdIdOrderByBidDesc(adId)){
			adBids.add(bidHist);
		}
		return adBids;
	}
	
	/**
	 * Find the user name by its user id.
	 * 
	 * @param userId
	 * 			the id to find the corresponding user name
	 * @return
	 * 			either the user name or "ghost user" if the user is null
	 */
	@Transactional
	public String getUserName(long userId) {
		if (userService.findUserById(userId)!=null){
			return userService.findUserById(userId).getUsername();
		}
		return "ghost user";
	}
	/**
	 * Gets the highest bid for every ad and add the value to a list.
	 * 
	 * @param ads
	 * 		The iterable to get the ads from
	 * @return
	 * 		List of bids
	 */
	
	public Iterable<Long> getBids(Iterable<Ad> ads){
		List<Long> bids = new ArrayList<Long>();
		long bidPrice;
		for(Ad ad : ads){
			bidPrice = getHighestBid(ad.getId());
			bids.add(bidPrice);
		}
		return bids;
	}
	
	
	/**
	 * Adds the user name to a bid.
	 * 
	 * @param bids
	 * 			The iterable of bids to add the user names
	 * @return
	 * 			A list of user names
	 */
	public Iterable<String> getBidUsernames(Iterable<Bid> bids){
		List<String> bidNames = new ArrayList<String>();
		long userID;
		String name;
		
		for(Bid bid : bids){
			userID=bid.getUserId();
			if(userService.findUserById(userID) != null){
					name = userService.findUserById(userID).getUsername();
			}
			else{	name = "ghost user"; }
		bidNames.add(name);
		}
		return bidNames;
	}
	
	
	/** 
	 * Checks whether a bid is the highest bid.
	 * 
	 * @param bidHistory
	 * 			the bid that is checked
	 * @return
	 * 			true if bidHistory is the highest bid, false otherwise
	 */
	public boolean isHighestBid(Bid bidHistory){
		Bid bidH = bidHistoryDao.findTop1ByadIdOrderByBidDesc( bidHistory.getAdId() );
		if(bidH == null) return true; 
		return bidHistory.getBid()>bidH.getBid();
	}
	
	/**
	 * Checks if a user is the one that placed the highest bid.
	 * 
	 * @param bid
	 * 		to find the user id that belongs to the highest bid and the user id of the current bidder
	 * @return
	 * 		true if the user is already the highest bidder, false otherwise
	 */
	public boolean isAlreadyHighestBidder(Bid bid){
		if (bidHistoryDao.findTop1ByadIdOrderByBidDesc(bid.getAdId())!=null){
			return bidHistoryDao.findTop1ByadIdOrderByBidDesc(bid.getAdId()).getUserId()==bid.getUserId();			
		}
		return false;
	}
	
	/**
	 * Checks whether a user is the creator of a bid.
	 * 
	 * @param bid
	 * 			the bid to check whether a user is the creator
	 * @return
	 * 			true if a user is the creator, false otherwise
	 */
	public boolean isBidCreator(Bid bid){
		if (adService.getAdById(bid.getAdId())!=null){
			return bid.getUserId() == adService.getAdById(bid.getAdId()).getUser().getId();
		}	
		return false;
	}
	
	/**
	 * Get the (last) bid of a current user for a given ad.
	 * 
	 * @param username
	 * 			the current user
	 * @param adId
	 * 			the ad to find the bid of the current user
	 * @return
	 * 			the value of the bid of the current user
	 */
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
	
	/**
	 * Calculates the next higher bid of a given ad.
	 * 
	 * @param adId
	 * 		the ad to calculate the next higher bid
	 * @return
	 * 		the start offer if no bid has been placed so far, the next higher bid otherwise
	 */
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
	
	/**
	 * Finds the highest bid of an ad.
	 * 
	 * @param adId
	 * 		the ad where to get the highest bid from
	 * @return
	 * 		0 if the ad does not exist,
	 * 		the start offer if no bid has been placed or
	 * 		the value of the newest bid
	 */
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
	
	/**
	 * Checks whether at least one bid has been placed at a given ad.
	 * 
	 * @param adId
	 * 		the ad to check whether there has been placed at least one bid
	 * @return
	 * 		true if a bid has been placed, false otherwise
	 */
	public boolean isBidden(long adId){
		return bidHistoryDao.findTop1ByadIdOrderByBidDesc(adId) != null;
	}
	
	/**
	 * Finds all bids that belong to a given ad.
	 * 
	 * @param adId
	 * 			The ad to find all corresponding bids
	 * @return
	 * 			All bids that belong to the given ad
	 */
	public Iterable<Bid> getAllBids(long adId){
		return bidHistoryDao.findByAdIdOrderByBidDesc(adId);
	}
	
}
