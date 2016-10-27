package ch.unibe.ese.team1.controller.service;

import java.util.Queue;
import java.util.Iterator;
import java.util.LinkedList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.BidHistory;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.dao.AlertDao;
import ch.unibe.ese.team1.model.dao.BidHistoryDao;
import ch.unibe.ese.team1.model.dao.MessageDao;
import static org.junit.Assert.assertTrue;

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
	
	@Transactional
	public void addBid(BidHistory newBid){
		Ad ad = adService.getAdById(newBid.getAdId());
		assertTrue("Ad must be of type auction",ad.getSellType()==3);
		if (ad.isAuctionAvailable() && isHighestBid(newBid)){
			User user = userService.findUserById(newBid.getUserId());
			user.getId();
			Queue<BidHistory> bidHist = new LinkedList<BidHistory>();
			bidHist.add(newBid);
//			Iterator<BidHistory> bidHistIter = bidHistoryDao.findByAdIdOrderByBid(newBid.getAdId()).iterator();
//			while(bidHistIter.hasNext()){
//				bidHist.add(bidHistIter.next());
//			}
			bidHistoryDao.save(bidHist);
		}
	}
	
	public boolean isHighestBid(BidHistory bidHistory){
//		BidHistory bidH = bidHistoryDao.findTop1ByadIdOrderByBid( bidHistory.getAdId() );
//		return bidHistory.getBid()>bidH.getBid();
		return true;
	}
	
	
}
