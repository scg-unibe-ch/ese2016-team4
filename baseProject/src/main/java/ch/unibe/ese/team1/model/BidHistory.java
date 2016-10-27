package ch.unibe.ese.team1.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import static org.junit.Assert.assertTrue;

import java.sql.Date;

/** Describes an advertisement that users can place and search for. */
@Entity
public class BidHistory {

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false)
	private long userId;
	
	@Column(nullable = false)
	private long adId;
	
	@Column(nullable = false)
	private long bid;
	
	@Column(nullable = false)
	private Date bidTime;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getAdId() {
		return adId;
	}

	public void setAdId(long adId) {
		this.adId = adId;
	}

	public long getBid() {
		return bid;
	}
	
	public void setBid(long bid) {
		assertTrue("Bid must be >0",bid>0);
		this.bid = bid;
	}
}
