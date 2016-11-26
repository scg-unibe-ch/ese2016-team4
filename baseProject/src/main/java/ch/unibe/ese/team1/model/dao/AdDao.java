package ch.unibe.ese.team1.model.dao;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.User;

public interface AdDao extends CrudRepository<Ad, Long> {

	/** this will be used if searched only for price*/
	public Iterable<Ad> findByPrizePerMonthLessThan (int prize);

	/** this will be used if selected propertyTypes are searched */
	public Iterable<Ad> findByPropertyTypeAndSellTypeAndPrizePerMonthLessThan(int propertyType, int sellType,
			int i);

	/** this will be used if selected propertyTypes are searched */
	public Iterable<Ad> findByPropertyTypeAndSellType(int propertyType, int sellType);

	public Iterable<Ad> findByUser(User user);
	
	public Iterable<Ad> findByAuctionEndDateLessThanAndAuctionFinished(Date date, boolean auctionFinished);
}
