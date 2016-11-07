package ch.unibe.ese.team1.controller.service;

import java.util.Queue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.unibe.ese.team1.controller.pojos.forms.SearchForm;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.BidHistory;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.dao.AdDao;
import ch.unibe.ese.team1.model.dao.AlertDao;
import ch.unibe.ese.team1.model.dao.BidHistoryDao;
import ch.unibe.ese.team1.model.dao.MessageDao;
import static org.junit.Assert.assertTrue;

@Service
public class BidHistoryService {
	@Autowired
	private UserService userService;

	@Autowired
	private AdService adService;
	
	@Autowired
	AlertDao alertDao;

	@Autowired
	MessageDao messageDao;

	@Autowired
	BidHistoryDao bidHistoryDao;
	
	@Autowired
	AdDao adDao;
	
	@Transactional
	public void addBid(long bidAdId, long bidUserId, long bidBid){
		
		BidHistory newBid = new BidHistory(bidAdId, bidUserId, bidBid);
		Ad ad = adService.getAdById(newBid.getAdId());
		assertTrue("Ad must be of type auction",ad.getSellType()==3);
		if (ad.isAuctionAvailable() &&
				isHighestBid(newBid) && 
				!isAlreadyHighestBidder(newBid)&&
				!isBidCreator(newBid)){
			User user = userService.findUserById(newBid.getUserId());
			user.getId();
			Queue<BidHistory> bidHist = new LinkedList<BidHistory>();
			bidHist.add(newBid);
			Iterator<BidHistory> bidHistIter = bidHistoryDao.findByAdIdOrderByBidDesc(newBid.getAdId()).iterator();
			while(bidHistIter.hasNext()){
				bidHist.add(bidHistIter.next());
			}
			bidHistoryDao.save(bidHist);
		}
	}
	
	@Transactional
	public Iterable<BidHistory> allBids(long adId) {
		List<BidHistory> adBids = new ArrayList<>();
		for (BidHistory bidHist : bidHistoryDao.findByAdIdOrderByBidDesc(adId)){
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
	
	public boolean isHighestBid(BidHistory bidHistory){
		BidHistory bidH = bidHistoryDao.findTop1ByadIdOrderByBidDesc( bidHistory.getAdId() );
		if(bidH == null) return true; 
		return bidHistory.getBid()>bidH.getBid();
	}
	
	public boolean isAlreadyHighestBidder(BidHistory bid){
		if (bidHistoryDao.findTop1ByadIdOrderByBidDesc(bid.getAdId())!=null){
			return bidHistoryDao.findTop1ByadIdOrderByBidDesc(bid.getAdId()).getUserId()==bid.getUserId();			
		}
		return false;
	}
	
	public boolean isBidCreator(BidHistory bid){
		if (adService.getAdById(bid.getAdId())!=null){
			return bid.getUserId() == adService.getAdById(bid.getAdId()).getUser().getId();
		}	
		return false;
	}
	
	public long getMyBid(String username, long adId){
		User user = userService.findUserByUsername(username);
		Iterable<BidHistory> bids = bidHistoryDao.findByAdIdOrderByBidDesc(adId);
		if (user!=null && bids!=null){
			for(BidHistory bid : bids){
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
		BidHistory bid = bidHistoryDao.findTop1ByadIdOrderByBidDesc(adId);
		if (bid==null){
			return 0;
		}else{
			return bid.getBid();			
		}
	}
	
	public Iterable<BidHistory> getAllBids(long adId){
		return bidHistoryDao.findByAdIdOrderByBidDesc(adId);
	}
	
	
}
