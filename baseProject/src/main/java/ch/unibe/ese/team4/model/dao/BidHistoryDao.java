package ch.unibe.ese.team4.model.dao;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.team4.model.Bid;

public interface BidHistoryDao extends CrudRepository<Bid, Long> {
	public Iterable<Bid> findByAdIdOrderByBidDesc(long adId);
	public Bid findTop1ByadIdOrderByBidDesc(long adId);
	
}
