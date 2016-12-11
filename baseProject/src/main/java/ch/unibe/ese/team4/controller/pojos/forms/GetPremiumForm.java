package ch.unibe.ese.team4.controller.pojos.forms;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


public class GetPremiumForm {
	
	@Min(value=1, message = "Please enter a valid month")
	@Max(value=12, message = "Please enter a valid month")
	private int ccMonth;
	
	@Min(value=0, message = "Please enter a valid year")
	private int ccYear;

	
	@Pattern(regexp = "\\b([0-9]{4})([0-9]{4})([0-9]{4})([0-9]{4})\\b", message = "Please enter a valid 16 digit credit card number")
	private String ccNumber;
		
	@NotNull
	private boolean premiumUser;
		
	public int getCcMonth() {
		return ccMonth;
	}

	public void setCcMonth(int ccMonth) {
		this.ccMonth = ccMonth;
	}

	public int getCcYear() {
		return ccYear;
	}

	public void setCcYear(int ccYear) {
		this.ccYear = ccYear;
	}

	public String getCcNumber() {
		return ccNumber;
	}

	public void setCcNumber(String ccNumber) {
		this.ccNumber = ccNumber;
	}
		
	public boolean isPremiumUser() {
		return premiumUser;
	}

	public void setPremiumUser(boolean premiumUser) {
		this.premiumUser = premiumUser;
	}

}
