package ch.unibe.ese.team1.controller.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import ch.unibe.ese.team1.model.BidHistory;
import ch.unibe.ese.team1.model.dao.AlertDao;
import ch.unibe.ese.team1.model.dao.MessageDao;
import ch.unibe.ese.team1.model.dao.UserDao;

public class BidHistoryService {
	@Autowired
	UserDao userDao;
	
	@Autowired
	AlertDao alertDao;

	@Autowired
	MessageDao messageDao;

	
	@Transactional
	public void addBid(BidHistory bidHist, long user){
		
	}
	
}
