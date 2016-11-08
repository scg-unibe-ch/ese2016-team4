package ch.unibe.ese.team1.model.dao;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.team1.model.Bid;

public interface BidHistoryDao extends CrudRepository<Bid, Long> {
	public Iterable<Bid> findByAdIdOrderByBidDesc(long adId);
	public Bid findTop1ByadIdOrderByBidDesc(long adId);
	
}
