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
	
	public boolean isHighestBid(BidHistory bidHistory){
		BidHistory bidH = bidHistoryDao.findTop1ByadIdOrderByBidDesc( bidHistory.getAdId() );
		if(bidH == null) return true; 
		return bidHistory.getBid()>bidH.getBid();
	}
	
	
}
