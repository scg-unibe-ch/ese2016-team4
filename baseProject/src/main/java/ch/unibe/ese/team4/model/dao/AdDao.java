package ch.unibe.ese.team4.model.dao;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.team4.model.Ad;
import ch.unibe.ese.team4.model.User;

public interface AdDao extends CrudRepository<Ad, Long> {

	/** this will be used if searched only for price*/
	public Iterable<Ad> findByPrizePerMonthLessThan (int prize);

	/** this will be used if selected propertyTypes are searched */
	public Iterable<Ad> findByPropertyTypeAndSellTypeAndPrizePerMonthLessThan(int propertyType, int sellType,
			int i);

	/** this will be used if selected propertyTypes are searched */
	public Iterable<Ad> findByPropertyTypeAndSellType(int propertyType, int sellType);

	/** this will be used to find the ads of a given user */
	public Iterable<Ad> findByUser(User user);
	
	/** this is used to check whether an auction has expired */
	public Iterable<Ad> findByAuctionEndDateLessThanAndAuctionFinished(Date date, boolean auctionFinished);
}
