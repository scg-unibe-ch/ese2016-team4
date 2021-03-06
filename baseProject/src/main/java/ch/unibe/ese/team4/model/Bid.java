package ch.unibe.ese.team4.model;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/** Describes an advertisement that users can place and search for. */
@Entity
public class Bid {
	
	public Bid(){
		
	}
	public Bid(long adId, long userId, long bid){
		this.adId = adId;
		this.userId= userId;
		this.bid = bid;
		this.bidTime = Calendar.getInstance();
	}

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
	private Calendar bidTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Calendar getBidTime() {
		return bidTime;
	}

	public void setBidTime(Calendar bidTime) {
		this.bidTime = bidTime;
	}
	
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
