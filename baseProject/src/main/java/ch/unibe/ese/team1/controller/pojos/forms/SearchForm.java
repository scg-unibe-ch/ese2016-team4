package ch.unibe.ese.team1.controller.pojos.forms;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import ch.unibe.ese.team1.model.Ad;

/** This form is used for searching for an ad. */
public class SearchForm {

	// variables as used in the database
	private static final int ROOM = 1, STUDIO = 2, FLAT = 3, HOUSE = 4;
	private static final int RENT = 1, BUY = 2, AUCTION = 3;
	
	
	private boolean buy, rent, auction;
	private boolean flat, house, studio, room;
	// list of all the propertytypes/selltypes for the database request
	List<Integer> propertyType = new ArrayList<>();
	List<Integer> sellType = new ArrayList<>();
	
	// lists get deleted after every search, to not get duplicated values
	public void deleteLists(){
		propertyType = new ArrayList<>();
		sellType = new ArrayList<>();
	}
	public List<Integer> getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(int propertyType) {
		this.propertyType.add(propertyType);
	}

	public List<Integer> getSellType() {
		return sellType;
	}

	public void setSellType(int sellType) {
		this.sellType.add(sellType);
	}
	
	public boolean getRoom() {
		return room;
	}

	public void setRoom(boolean room) {
		this.room = room;
		if(room && !propertyType.contains(ROOM)) setPropertyType(ROOM);
	}
	
	public boolean getStudio() {
		return studio;
	}

	public void setStudio(boolean studio) {
		this.studio = studio;
		if(studio && !propertyType.contains(STUDIO))setPropertyType(STUDIO);
	}
	
	public boolean getFlat() {
		return flat;
	}

	public void setFlat(boolean flat) {
		this.flat = flat;
		if(flat && !propertyType.contains(FLAT)) setPropertyType(FLAT);
	}

	public boolean getHouse() {
		return house;
	}

	public void setHouse(boolean house) {
		this.house = house;
		if(house && !propertyType.contains(HOUSE)) setPropertyType(HOUSE);
	}

	public boolean getAuction() {
		return auction;
	}

	public void setAuction(boolean auction) {
		this.auction = auction;
		if(auction && !sellType.contains(AUCTION)) setSellType(AUCTION);
	}
	
	public boolean getBuy() {
		return buy;
	}

	public void setBuy(boolean buy) {
		this.buy = buy;
		if(buy && !sellType.contains(BUY)) setSellType(BUY);
	}
	
	public boolean getRent() {
		return rent;
	}

	public void setRent(boolean rent) {
		this.rent = rent;
		if(rent && !sellType.contains(RENT)) setSellType(RENT);
	}


	@NotBlank(message = "Required")
	@Pattern(regexp = "^[0-9]{4} - [-\\w\\s\\u00C0-\\u00FF]*", message = "Please pick a city from the list")
	private String city;
	
	@NotNull(message = "Requires a number")
	@Min(value = 0, message = "Please enter a positive distance")
	private Integer radius;
	
	@NotNull(message = "Requires a number")
	@Min(value = 0, message = "In your dreams.")
	private Integer prize;

	
	@AssertFalse(message = "Please select either or both types")
	private boolean noRoomNoStudio;


	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getRadius() {
		return radius;
	}

	public void setRadius(Integer radius) {
		this.radius = radius;
	}

	public Integer getPrize() {
		return prize;
	}

	public void setPrize(Integer prize) {
		this.prize = prize;
	}

	// //////////////////
	// Filtered results//
	// //////////////////

	private String earliestMoveInDate;
	private String latestMoveInDate;
	private String earliestMoveOutDate;
	private String latestMoveOutDate;

	private boolean smokers;
	private boolean animals;
	private boolean garden;
	private boolean balcony;
	private boolean cellar;
	private boolean furnished;
	private boolean cable;
	private boolean garage;
	private boolean internet;


	// the ugly stuff

	public boolean getSmokers() {
		return smokers;
	}

	public void setSmokers(boolean smokers) {
		this.smokers = smokers;
	}

	public boolean getAnimals() {
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

	public boolean getFurnished() {
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

	public String getEarliestMoveInDate() {
		return earliestMoveInDate;
	}

	public void setEarliestMoveInDate(String earliestMoveInDate) {
		this.earliestMoveInDate = earliestMoveInDate;
	}

	public String getLatestMoveInDate() {
		return latestMoveInDate;
	}

	public void setLatestMoveInDate(String latestMoveInDate) {
		this.latestMoveInDate = latestMoveInDate;
	}

	public String getEarliestMoveOutDate() {
		return earliestMoveOutDate;
	}

	public void setEarliestMoveOutDate(String earliestMoveOutDate) {
		this.earliestMoveOutDate = earliestMoveOutDate;
	}

	public String getLatestMoveOutDate() {
		return latestMoveOutDate;
	}

	public void setLatestMoveOutDate(String latestMoveOutDate) {
		this.latestMoveOutDate = latestMoveOutDate;
	}
	
	
}