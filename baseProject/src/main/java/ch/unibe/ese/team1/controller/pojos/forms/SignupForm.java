package ch.unibe.ese.team1.controller.pojos.forms;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import ch.unibe.ese.team1.model.Gender;

/** This form is used when a user want to sign up for an account. */
public class SignupForm {
	
	@Size(min = 6, message = "Password must be at least 6 characters long")
	@NotNull
	private String password;

	@Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message = "Must be valid email address")
	@NotNull
	private String email;

	@Pattern(regexp = "[a-zA-Z]+", message = "First name must be a valid name")
	@NotNull
	private String firstName;

	@Pattern(regexp = "[a-zA-Z]+", message = "Last name must be a valid name")
	@NotNull
	private String lastName;
	
	@NotNull
	private Gender gender;
	
	//@Pattern(regexp = "(0[1-9]|1[012])", message = "Please enter a valid month")
	@Min(value=0, message = "Please enter a valid month")
	@Max(value=12, message = "Please enter a valid month")
	private int ccMonth;
	
	//@Pattern(regexp = "^$|^(20)\\d{2}$", message = "Please enter a valid year")
	private int ccYear;

	
	@Pattern(regexp = "^$|^\\d{16}$", message = "Please enter a valid 16 digit credit card number")
	private String ccNumber;
	
	@NotNull
	private boolean premiumUser;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}
	
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
