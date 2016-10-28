package ch.unibe.ese.team1.model.dao;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.team1.model.BidHistory;

public interface BidHistoryDao extends CrudRepository<BidHistory, Long> {
	public Iterable<BidHistory> findByAdIdOrderByBidDesc(long adId);
	public BidHistory findTop1ByadIdOrderByBidDesc(long adId);
	
}
