package ch.unibe.ese.team4.controller.pojos.forms;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

/** This form is used when a user wants to place a new ad. */
public class PlaceAdForm {


	@Min(value=1, message = "YOU SHALL CHOOSE")
	private int propertyType;

	@Min(value=1, message = "YOU SHALL CHOOSE")
	private int sellType;
	
	@Pattern(regexp= "^$|^(((0[1-9]|[12]\\d|3[01])\\.(0[13578]|1[02])\\.((1[6-9]|[2-9]\\d)\\d{2}))|((0[1-9]|[12]\\d|30)\\.(0[13456789]|1[012])\\.((1[6-9]|[2-9]\\d)\\d{2}))|((0[1-9]|1\\d|2[0-8])\\.02\\.((1[6-9]|[2-9]\\d)\\d{2}))|(29\\.02\\.((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))))$",message = "dd.MM.yyyy")
	private String auctionEndDate;

	@NotBlank(message = "Required")
	private String title;

	@NotBlank(message = "Required")
	private String street;

	@Pattern(regexp = "^[0-9]{4} - [-\\w\\s\\u00C0-\\u00FF]*", message = "Please pick a city from the list")
	private String city;
	
	@Pattern(regexp="^$|^(((0[1-9]|[12]\\d|3[01])\\.(0[13578]|1[02])\\.((1[6-9]|[2-9]\\d)\\d{2}))|((0[1-9]|[12]\\d|30)\\.(0[13456789]|1[012])\\.((1[6-9]|[2-9]\\d)\\d{2}))|((0[1-9]|1\\d|2[0-8])\\.02\\.((1[6-9]|[2-9]\\d)\\d{2}))|(29\\.02\\.((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))))$",message = "dd.MM.yyyy")
	private String moveInDate;
	
	@Pattern(regexp="^$|^(((0[1-9]|[12]\\d|3[01])\\.(0[13578]|1[02])\\.((1[6-9]|[2-9]\\d)\\d{2}))|((0[1-9]|[12]\\d|30)\\.(0[13456789]|1[012])\\.((1[6-9]|[2-9]\\d)\\d{2}))|((0[1-9]|1\\d|2[0-8])\\.02\\.((1[6-9]|[2-9]\\d)\\d{2}))|(29\\.02\\.((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))))$",message = "dd.MM.yyyy")
	private String moveOutDate;

	@Min(value=0, message = "Negative prizes aren't allowed")
	private int prize;
	
	@Min(value=0, message = "Negative prizes aren't allowed")
	private int instantBuyPrice;

	@Min(value=0, message = "Negative prizes aren't allowed")
	private int prizeOfSale;
	
	@Min(value=0, message = "Negative start offers aren't allowed")
	private int startOffer;

	@Min(value=0, message = "Please enter a positive Number")
	private int squareFootage;

	@NotBlank(message = "Required")
	private String roomDescription;

	private String preferences;

	// optional free text description
	private String roommates;

	// First user are added as strings, then transformed
	// to Users and added to the DB in through adService
	private List<String> registeredRoommateEmails;

	// optional for input
	private String roomFriends;

	private boolean smokers;
	private boolean animals;
	private boolean garden;
	private boolean balcony;
	private boolean cellar;
	private boolean furnished;
	private boolean cable;
	private boolean garage;
	private boolean internet;
	private boolean dishwasher;
	private boolean washingMachine;

	private List<String> visits;


	public int getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(int propertyType) {
		this.propertyType = propertyType;
	}

	public int getSellType() {
		return sellType;
	}

	public void setSellType(int sellType) {
		this.sellType = sellType;
	}
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getPrize() {
		return prize;
	}

	public void setPrize(int prize) {
		this.prize = prize;
	}
	
	public int getInstantBuyPrice() {
		return instantBuyPrice;
	}

	public void setInstantBuyPrice(int instantBuyPrice) {
		this.instantBuyPrice = instantBuyPrice;
	}

	public String getRoomDescription() {
		return roomDescription;
	}

	public void setRoomDescription(String roomDescription) {
		this.roomDescription = roomDescription;
	}

	public String getPreferences() {
		return preferences;
	}

	public void setPreferences(String preferences) {
		this.preferences = preferences;
	}

	public int getSquareFootage() {
		return squareFootage;
	}

	public void setSquareFootage(int squareFootage) {
		this.squareFootage = squareFootage;
	}

	public String getRoommates() {
		return roommates;
	}

	public void setRoommates(String roommates) {
		this.roommates = roommates;
	}

	public boolean isSmokers() {
		return smokers;
	}

	public void setSmokers(boolean smoker) {
		this.smokers = smoker;
	}

	public boolean isAnimals() {
		return animals;
	}

	public void setAnimals(boolean animals) {
		this.animals = animals;
	}

	public boolean getGarden() {
		return garden;
	}

	public void setGarden(boolean garden) {
		this.garden = garden;
	}

	public boolean getBalcony() {
		return balcony;
	}

	public void setBalcony(boolean balcony) {
		this.balcony = balcony;
	}

	public boolean getCellar() {
		return cellar;
	}

	public void setCellar(boolean cellar) {
		this.cellar = cellar;
	}

	public boolean isFurnished() {
		return furnished;
	}

	public void setFurnished(boolean furnished) {
		this.furnished = furnished;
	}

	public boolean getCable() {
		return cable;
	}

	public void setCable(boolean cable) {
		this.cable = cable;
	}

	public boolean getGarage() {
		return garage;
	}

	public void setGarage(boolean garage) {
		this.garage = garage;
	}

	public boolean getInternet() {
		return internet;
	}

	public void setInternet(boolean internet) {
		this.internet = internet;
	}
	
	public boolean getDishwasher() {
		return dishwasher;
	}
	
	public void setDishwasher(boolean dishwasher) {
		this.dishwasher = dishwasher;
	}
	
	public boolean getWashingMachine() {
		return washingMachine;
	}
	
	public void setWashingMachine(boolean washingMachine) {
		this.washingMachine = washingMachine;
	}

	public String getMoveInDate() {
		return moveInDate;
	}

	public void setMoveInDate(String moveInDate) {
		this.moveInDate = moveInDate;
	}
	public void setMoveInDate(Date moveInDate) {
		if (moveInDate != null){
			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
			this.moveInDate = dateFormat.format(moveInDate);
		}
	}

	public String getMoveOutDate() {
		return moveOutDate;
	}

	public void setMoveOutDate(String moveOutDate) {
		this.moveOutDate = moveOutDate;
	}
	
	public void setMoveOutDate(Date moveOutDate) {
		if (moveOutDate != null){
			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
			this.moveOutDate = dateFormat.format(moveOutDate);
		}
	}
	
	public String getAuctionEndDate() {
		return auctionEndDate;
	}
	
	public void setAuctionEndDate(String auctionEndDate) {
		this.auctionEndDate = auctionEndDate;
	}
	
	public void setAuctionEndDate(Date auctionEndDate) {
		if (auctionEndDate != null){
			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
			this.auctionEndDate = dateFormat.format(auctionEndDate);
		}
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getRoomFriends() {
		return roomFriends;
	}

	public void setRoomFriends(String roomFriends) {
		this.roomFriends = roomFriends;
	}
	
	public List<String> getRegisteredRoommateEmails() {
		return registeredRoommateEmails;
	}

	public void setRegisteredRoommateEmails(List<String> registeredRoommateEmails) {
		this.registeredRoommateEmails = registeredRoommateEmails;
	}

	public List<String> getVisits() {
		return visits;
	}

	public void setVisits(List<String> visits) {
		this.visits = visits;
	}
	
	public int getStartOffer() {
		return startOffer;
	}

	public void setStartOffer(int startOffer) {
		this.startOffer = startOffer;
	}

	public int getPrizeOfSale() {
		return prizeOfSale;
	}

	public void setPrizeOfSale(int prizeOfSale) {
		this.prizeOfSale = prizeOfSale;
	}
}
