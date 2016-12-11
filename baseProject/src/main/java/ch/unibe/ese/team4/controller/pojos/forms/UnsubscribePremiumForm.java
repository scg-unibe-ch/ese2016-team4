package ch.unibe.ese.team4.controller.pojos.forms;

import javax.persistence.Column;
import javax.validation.constraints.Min;

public class UnsubscribePremiumForm {

	private int reason;
	
	private boolean expensive;
	private boolean noUse;
	private boolean badService;
	private boolean otherReasons;
	
	@Min(value=1, message = "Please choose one")
	private int unsubscribeReason;

	public int getUnsubscribeReason() {
		return unsubscribeReason;
	}

	public void setUnsubscribeReason(int unsubscribeReason) {
		this.unsubscribeReason = unsubscribeReason;
	}


	public int getReason() {
		return reason;
	}

	public void setReason(int reason) {
		this.reason = reason;
	}

	public boolean isExpensive() {
		return expensive;
	}

	public void setExpensive(boolean expensive) {
		this.expensive = expensive;
	}

	public boolean isNoUse() {
		return noUse;
	}

	public void setNoUse(boolean noUse) {
		this.noUse = noUse;
	}

	public boolean isBadService() {
		return badService;
	}

	public void setBadService(boolean badService) {
		this.badService = badService;
	}

	public boolean isOtherReasons() {
		return otherReasons;
	}

	public void setOtherReasons(boolean otherReasons) {
		this.otherReasons = otherReasons;
	}

}
