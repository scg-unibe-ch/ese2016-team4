package ch.unibe.ese.team4.model.dao;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.team4.model.Bid;

public interface BidHistoryDao extends CrudRepository<Bid, Long> {
	
	/** Finds all bids that belong to a given ad and orders it from the highest to the lowest bid. */
	public Iterable<Bid> findByAdIdOrderByBidDesc(long adId);
	
	/** Finds the highest bid that belongs to a given ad. */
	public Bid findTop1ByadIdOrderByBidDesc(long adId);
	
}
