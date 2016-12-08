package ch.unibe.ese.team4.controller.pojos.forms;

import javax.validation.constraints.Min;

public class BidForm {
	
	@Min(value = 1, message = "Please enter a valid bid > 0")
	private long bid;

	public long getBid() {
		return bid;
	}

	public void setBid(long bid) {
		this.bid = bid;
	}
	
}
