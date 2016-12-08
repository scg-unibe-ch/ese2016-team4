package ch.unibe.ese.team4.controller.pojos.forms;

public class UnsubscribePremiumForm {

	private int reason;
	
	private boolean expensive;
	private boolean noUse;
	private boolean badService;
	private boolean otherReasons;

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
